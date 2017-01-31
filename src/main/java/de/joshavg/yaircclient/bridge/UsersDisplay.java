package de.joshavg.yaircclient.bridge;

import de.joshavg.yaircclient.api.Client;
import de.joshavg.yaircclient.api.Message;
import de.joshavg.yaircclient.api.ResponseParser;
import de.joshavg.yaircclient.api.listener.ApiListener;
import de.joshavg.yaircclient.gui.*;

import java.util.Map;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class UsersDisplay implements ApiListener, GuiListener {

    private final Client client;
    private final OutputFactory outputFactory;
    private String channel;

    @Inject
    UsersDisplay(Client client, OutputFactory outputFactory) {
        this.client = client;
        this.outputFactory = outputFactory;
    }

    @Override
    public void parsed(Map<ResponseParser.Key, ResponseParser.ResponseValue> parsed, Client client) {
        String cmd = parsed.get(ResponseParser.Key.CMD).get();
        String[] meta = parsed.get(ResponseParser.Key.META).getValues();

        if (meta.length < 2 || !meta[1].equals(channel)) {
            return;
        }

        if ("353".equals(cmd)) {
            outputFactory.get(channel).writeln(parsed.get(ResponseParser.Key.PAYLOAD).get(), ActionType.NOTICE);
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
            return;
        }

        channel = target.getTarget();
        client.write(Message.names(channel));
    }
}
