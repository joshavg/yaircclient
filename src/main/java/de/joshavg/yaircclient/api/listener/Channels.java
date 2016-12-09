package de.joshavg.yaircclient.api.listener;

import de.joshavg.yaircclient.api.Client;
import de.joshavg.yaircclient.api.ResponseParser;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Channels implements ApiListener {

    private Set<String> channels = new HashSet<>();

    @Override
    public void parsed(Map<ResponseParser.Key, ResponseParser.ResponseValue> parsed, Client client) {
        String command = parsed.get(ResponseParser.Key.CMD).get();
        String target = parsed.get(ResponseParser.Key.TARGET).get();
        String nick = parsed.get(ResponseParser.Key.NICK).get();
        boolean isMe = client.getNick().equals(nick);

        if (!isMe) {
            return;
        }

        if (command.equals("JOIN")) {
            channels.add(target);
        } else if (command.equals("PART")) {
            channels.remove(target);
        }
    }
}
