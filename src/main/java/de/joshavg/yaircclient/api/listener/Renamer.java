package de.joshavg.yaircclient.api.listener;

import de.joshavg.yaircclient.api.Client;
import de.joshavg.yaircclient.api.Message;
import de.joshavg.yaircclient.api.ResponseParser;

import javax.inject.Singleton;
import java.util.Map;

@Singleton
public class Renamer implements ApiListener {

    private int tries;

    private String originalNick = null;

    @Override
    public void parsed(Map<ResponseParser.Key, ResponseParser.ResponseValue> parsed, Client client) {
        if ("433".equals(parsed.get(ResponseParser.Key.CMD).get())) {
            if (originalNick == null) {
                originalNick = client.getNick();
            }

            tries++;
            String newNick = originalNick + tries;
            client.write(Message.nick(newNick));
            client.setNick(newNick);
        }
    }
}
