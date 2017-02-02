package de.joshavg.yaircclient.api;

import de.joshavg.yaircclient.api.listener.ApiListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class Client implements ApiListener {

    private static final Logger LOG = LoggerFactory.getLogger(Client.class);
    private final Charset utf8Charset;
    private String nick;
    private Set<ApiListener> listeners;
    private Socket socket;
    private BufferedWriter writer;
    private boolean isConnected;

    Client() {
        listeners = new HashSet<>();
        utf8Charset = StandardCharsets.UTF_8;
    }

    public void addListener(ApiListener l) {
        this.listeners.add(l);
    }

    public void connect(String host, String nick, int port) {
        this.nick = nick;
        if (socket != null && !socket.isClosed()) {
            throw new IllegalStateException("socket is already in use");
        }

        new Thread(() -> {
            try {
                socket = new Socket(host, port);
                socket.setKeepAlive(true);

                BufferedReader reader = new BufferedReader(
                    new InputStreamReader(socket.getInputStream(), utf8Charset));
                writer = new BufferedWriter(
                    new OutputStreamWriter(socket.getOutputStream(), utf8Charset));

                publishIdent(nick);
                handleSocketLifecycle(reader);
            } catch (IOException e) {
                LOG.error("error in socket", e);
                isConnected = false;
            }
        }).start();
    }

    private void publishIdent(String nick) {
        write(Message.user(nick));
        write(Message.nick(nick));
    }

    private void handleSocketLifecycle(BufferedReader reader) throws IOException {
        String line;
        while ((line = reader.readLine()) != null) {
            isConnected = true;
            handleServerResponse(line);
        }
    }

    private void handleServerResponse(String line) {
        LOG.debug("incoming: {}", line);
        Map<ResponseParser.Key, ResponseParser.ResponseValue> parsed = new ResponseParser(line)
            .parse();
        LOG.debug("parsed: {}", parsed);

        if ("376".equals(parsed.get(ResponseParser.Key.CMD).get())) {
            listeners.forEach(l -> l.connected(this));
        } else {
            listeners.forEach(l -> l.parsed(parsed, this));
        }
    }

    public boolean isConnected() {
        return isConnected;
    }

    public void write(String line) {
        LOG.debug("outgoing: {}", line);
        if (writer == null) {
            throw new IllegalStateException("socket not yet writable");
        }

        try {
            writer.write(line + "\n");
            writer.flush();
        } catch (IOException e) {
            LOG.error("error writing to socket", e);
        }
    }

    @Override
    public void parsed(Map<ResponseParser.Key, ResponseParser.ResponseValue> parsed,
        Client client) {
        if ("PING".equals(parsed.get(ResponseParser.Key.CMD).get())) {
            write("PONG " + parsed.get(ResponseParser.Key.PAYLOAD));
        }
    }

    public void quit() {
        if (socket == null) {
            return;
        }

        listeners.forEach(l -> l.quitCalled(this));

        try {
            socket.close();
        } catch (IOException e) {
            LOG.error("error closing socket", e);
        }
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }
}
