package de.joshavg.yaircclient.bridge;

import de.joshavg.yaircclient.api.Client;
import de.joshavg.yaircclient.api.Message;
import de.joshavg.yaircclient.api.ResponseParser.Key;
import de.joshavg.yaircclient.api.ResponseParser.ResponseValue;
import de.joshavg.yaircclient.api.listener.ApiListener;
import de.joshavg.yaircclient.gui.ActionType;
import de.joshavg.yaircclient.gui.GuiListener;
import de.joshavg.yaircclient.gui.MainForm;
import de.joshavg.yaircclient.gui.OutputFactory;
import de.joshavg.yaircclient.gui.OutputTarget;
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
    public void parsed(Map<Key, ResponseValue> parsed, Client client) {
        String cmd = parsed.get(Key.CMD).get();
        String parsedChannel = parsed.get(Key.CHANNEL).get();

        if ("366".equals(cmd)) {
            channel = "";
        }

        if (!parsedChannel.equals(channel)) {
            return;
        }

        if ("353".equals(cmd)) {
            outputFactory.get(channel)
                .writeln(parsed.get(Key.PAYLOAD).get(), ActionType.NOTICE);
        }
    }

    @Override
    public void messageTyped(String message, MainForm gui) {
        OutputTarget target = gui.getCurrentTarget();
        boolean isChannel = target.getTarget().startsWith("#");

        if (!"/u?".equals(message) || !isChannel) {
            return;
        }

        channel = target.getTarget();
        client.write(Message.names(channel));
    }
}
