package de.joshavg.yaircclient.api.listener;

import de.joshavg.yaircclient.api.Client;
import de.joshavg.yaircclient.api.Message;
import de.joshavg.yaircclient.api.ResponseParser;
import de.joshavg.yaircclient.api.listener.ApiListener;

import java.util.Map;

public class Renamer implements ApiListener {

    private int tries;

    @Override
    public void parsed(Map<ResponseParser.Key, ResponseParser.ResponseValue> parsed, Client client) {
        if(parsed.get(ResponseParser.Key.CMD).get().equals("433")) {
            tries++;
            String newNick = client.getNick() + tries;
            client.write(Message.nick(newNick));
        }
    }
}
