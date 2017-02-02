package de.joshavg.yaircclient.api.listener;

import de.joshavg.yaircclient.api.Client;
import de.joshavg.yaircclient.api.ResponseParser;
import java.util.Map;

public interface ApiListener {

    default void connected(Client client) {
    }

    default void parsed(Map<ResponseParser.Key, ResponseParser.ResponseValue> parsed,
        Client client) {
    }

    default void quitCalled(Client client) {
    }
}
