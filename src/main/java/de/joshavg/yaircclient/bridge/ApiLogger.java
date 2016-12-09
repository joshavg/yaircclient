package de.joshavg.yaircclient.bridge;

import de.joshavg.yaircclient.api.Client;
import de.joshavg.yaircclient.api.ResponseParser;
import de.joshavg.yaircclient.api.listener.ApiListener;
import de.joshavg.yaircclient.gui.ActionType;
import de.joshavg.yaircclient.gui.OutputFactory;
import de.joshavg.yaircclient.gui.OutputTarget;

import java.util.Map;

public class ApiLogger implements ApiListener {
    @Override
    public void parsed(Map<ResponseParser.Key, ResponseParser.ResponseValue> parsed, Client client) {
        OutputTarget target = OutputFactory.getSystem();

        String message = String.format("%1s: %2s",
                parsed.get(ResponseParser.Key.CMD),
                parsed.get(ResponseParser.Key.PAYLOAD));
        target.writeln(message, ActionType.NOTICE);
    }
}
