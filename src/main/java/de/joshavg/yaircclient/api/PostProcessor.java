package de.joshavg.yaircclient.api;

import java.util.HashMap;
import java.util.Map;

import static de.joshavg.yaircclient.api.ResponseParser.Key.*;


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

        String cmd = map.get(CMD).get();
        if (methods.containsKey(cmd)) {
            methods.get(cmd).run();
        }
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