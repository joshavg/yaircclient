package de.joshavg.yaircclient.api;

import static de.joshavg.yaircclient.api.ResponseParser.Key.CHANNEL;
import static de.joshavg.yaircclient.api.ResponseParser.Key.CMD;
import static de.joshavg.yaircclient.api.ResponseParser.Key.META;
import static de.joshavg.yaircclient.api.ResponseParser.Key.NICK;
import static de.joshavg.yaircclient.api.ResponseParser.Key.RESPOND_TO;
import static de.joshavg.yaircclient.api.ResponseParser.Key.SENDER;
import static de.joshavg.yaircclient.api.ResponseParser.Key.TARGET;

import java.util.HashMap;
import java.util.Map;


class PostProcessor {

    private final Map<ResponseParser.Key, ResponseParser.ResponseValue> map;

    PostProcessor(Map<ResponseParser.Key, ResponseParser.ResponseValue> parsed) {
        this.map = parsed;
    }

    void postprocess() {
        Map<String, Runnable> methods = new HashMap<>();
        methods.put("JOIN", this::postProcessJoin);
        methods.put("PRIVMSG", this::postProcessPrivmsg);
        methods.put("PART", this::postProcessPart);
        methods.put("353", this::postProcessNames);
        methods.put("366", this::postProcessNamesEnd);

        String cmd = map.get(CMD).get();
        if (methods.containsKey(cmd)) {
            methods.get(cmd).run();
        }
    }

    private void postProcessNamesEnd() {
        map.put(CHANNEL, map.get(META));
    }

    private void postProcessNames() {
        map.put(CHANNEL, ResponseParser.ResponseValue.of(map.get(META).getValues()[1]));
    }

    private void postProcessPart() {
        map.put(CHANNEL, map.get(TARGET));
    }

    private void postProcessJoin() {
        postProcessPart();
        map.put(RESPOND_TO, map.get(TARGET));
    }

    private void postProcessPrivmsg() {
        String target = map.get(TARGET).get();
        if (target.startsWith("#")) {
            map.put(RESPOND_TO, map.get(TARGET));
        } else {
            map.put(RESPOND_TO, map.get(NICK));
        }
        map.put(SENDER, map.get(NICK));
    }
}
