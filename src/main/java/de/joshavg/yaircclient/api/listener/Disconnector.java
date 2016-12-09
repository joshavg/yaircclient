package de.joshavg.yaircclient.api.listener;

import de.joshavg.yaircclient.api.Client;
import de.joshavg.yaircclient.api.ResponseParser;

import java.util.Map;

public class Disconnector implements ApiListener {
    @Override
    public void parsed(Map<ResponseParser.Key, ResponseParser.ResponseValue> parsed, Client client) {
        if (parsed.get(ResponseParser.Key.CMD).get().equals("ERROR")) {
            client.quit();
        }
    }
}
