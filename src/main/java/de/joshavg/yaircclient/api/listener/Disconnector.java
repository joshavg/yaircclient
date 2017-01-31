package de.joshavg.yaircclient.api.listener;

import de.joshavg.yaircclient.api.Client;
import de.joshavg.yaircclient.api.ResponseParser;

import javax.inject.Singleton;
import java.util.Map;

@Singleton
public class Disconnector implements ApiListener {
    @Override
    public void parsed(Map<ResponseParser.Key, ResponseParser.ResponseValue> parsed, Client client) {
        if ("ERROR".equals(parsed.get(ResponseParser.Key.CMD).get())) {
            client.quit();
        }
    }
}
