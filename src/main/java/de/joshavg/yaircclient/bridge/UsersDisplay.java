package de.joshavg.yaircclient.bridge;

import de.joshavg.yaircclient.api.Client;
import de.joshavg.yaircclient.api.Message;
import de.joshavg.yaircclient.api.ResponseParser;
import de.joshavg.yaircclient.api.listener.ApiListener;
import de.joshavg.yaircclient.gui.*;

import java.util.Map;

public class UsersDisplay implements ApiListener, GuiListener {

    private Client client;

    private String channel;

    @Override
    public void connected(Client client) {
        this.client = client;
    }

    @Override
    public void parsed(Map<ResponseParser.Key, ResponseParser.ResponseValue> parsed, Client client) {
        String cmd = parsed.get(ResponseParser.Key.CMD).get();
        String[] meta = parsed.get(ResponseParser.Key.META).getValues();

        if (meta.length < 2 || !meta[1].equals(channel)) {
            return;
        }

        if ("353".equals(cmd)) {
            OutputFactory.get(channel).writeln(parsed.get(ResponseParser.Key.PAYLOAD).get(), ActionType.NOTICE);
        } else if("366".equals(cmd)) {
            channel = "";
        }
    }

    @Override
    public void messageTyped(String message, MainForm gui) {
        OutputTarget target = gui.getCurrentTarget();
        boolean isChannel = target.getTarget().startsWith("#");

        if (!"/u?".equals(message) || !isChannel) {
            return;
        }

        if (client == null) {
            target.writeln("not connected", ActionType.ERROR);
        }

        channel = target.getTarget();
        client.write(Message.names(channel));
    }
}
