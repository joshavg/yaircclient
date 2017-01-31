package de.joshavg.yaircclient.bridge;

import de.joshavg.yaircclient.api.Client;
import de.joshavg.yaircclient.api.ResponseParser;
import de.joshavg.yaircclient.api.listener.ApiListener;
import de.joshavg.yaircclient.gui.ActionType;
import de.joshavg.yaircclient.gui.OutputFactory;
import de.joshavg.yaircclient.gui.OutputTarget;

import java.util.Map;
import javax.inject.Inject;

public class JoinDisplay implements ApiListener {
    private final OutputFactory outputFactory;

    @Inject
    public JoinDisplay(OutputFactory outputFactory) {
        this.outputFactory = outputFactory;
    }

    @Override
    public void parsed(Map<ResponseParser.Key, ResponseParser.ResponseValue> parsed, Client client) {
        String command = parsed.get(ResponseParser.Key.CMD).get();
        if (!"JOIN".equals(command)) {
            return;
        }

        String nick = parsed.get(ResponseParser.Key.NICK).get();
        String channel = parsed.get(ResponseParser.Key.RESPOND_TO).get();
        String user = parsed.get(ResponseParser.Key.USER).get();

        String message = String.format("%s (%s) joined", nick, user);
        OutputTarget target = outputFactory.getOrCreate(channel);
        target.writeln(message, ActionType.CHANNEL);
    }
}
