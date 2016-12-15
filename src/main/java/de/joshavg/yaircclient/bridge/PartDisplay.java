package de.joshavg.yaircclient.bridge;

import de.joshavg.yaircclient.api.Client;
import de.joshavg.yaircclient.api.ResponseParser;
import de.joshavg.yaircclient.api.listener.ApiListener;
import de.joshavg.yaircclient.gui.ActionType;
import de.joshavg.yaircclient.gui.OutputFactory;
import de.joshavg.yaircclient.gui.OutputTarget;

import java.util.Map;

public class PartDisplay implements ApiListener {
    @Override
    public void parsed(Map<ResponseParser.Key, ResponseParser.ResponseValue> parsed, Client client) {
        String command = parsed.get(ResponseParser.Key.CMD).get();
        if (!command.equals("PART")) {
            return;
        }

        String nick = parsed.get(ResponseParser.Key.NICK).get();
        String channel = parsed.get(ResponseParser.Key.CHANNEL).get();
        String user = parsed.get(ResponseParser.Key.USER).get();

        String message = String.format("%s (%s) parted", nick, user);
        OutputTarget target = OutputFactory.getOrCreate(channel);
        target.writeln(message, ActionType.CHANNEL);
    }
}
