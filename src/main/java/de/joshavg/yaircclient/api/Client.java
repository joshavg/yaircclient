package de.joshavg.yaircclient.api;

import de.joshavg.yaircclient.api.listener.ApiListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Client implements ApiListener {

    private static final Logger LOG = LoggerFactory.getLogger(Client.class);

    private String nick;

    private Set<ApiListener> listeners;

    private Socket socket;

    private BufferedWriter writer;

    private boolean isConnected;

    Client() {
        this.listeners = new HashSet<>();
    }

    public void addListener(ApiListener l) {
        this.listeners.add(l);
    }

    public void connect(String host, String nick, int port) {
        this.nick = nick;

        new Thread(() -> {
            try {
                socket = new Socket(host, port);
                socket.setKeepAlive(true);

                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
                writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"));

                write(Message.user(nick));
                write(Message.nick(nick));

                String line;
                while ((line = reader.readLine()) != null) {
                    isConnected = true;

                    handleServerResponse(line);
                }
            } catch (IOException e) {
                LOG.error("error in socket", e);
                isConnected = false;
            }
        }).start();
    }

    private void handleServerResponse(String line) {
        LOG.trace("incoming: " + line);
        Map<ResponseParser.Key, ResponseParser.ResponseValue> parsed = new ResponseParser(line).parse();
        LOG.trace("parsed: " + parsed);

        if (parsed.get(ResponseParser.Key.CMD).get().equals("376")) {
            listeners.forEach(l -> l.connected(this));
        } else {
            listeners.forEach(l -> l.parsed(parsed, this));
        }
    }

    public boolean isConnected() {
        return isConnected;
    }

    public void write(String line) {
        LOG.trace("outgoing: " + line);
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
    public void parsed(Map<ResponseParser.Key, ResponseParser.ResponseValue> parsed, Client client) {
        if (parsed.get(ResponseParser.Key.CMD).get().equals("PING")) {
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
}
