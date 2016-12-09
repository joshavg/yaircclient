package de.joshavg.yaircclient.gui;

import java.util.HashMap;
import java.util.Map;

public class OutputFactory {

    private static Map<String, OutputTarget> TARGETS = new HashMap<>();

    private OutputFactory() {
    }

    public static OutputTarget getOrCreate(String name) {
        if (TARGETS.containsKey(name)) {
            return TARGETS.get(name);
        }

        OutputTarget pane = new OutputTarget();
        TARGETS.put(name, pane);

        return pane;
    }

    public static OutputTarget getSystem() {
        return TARGETS.get("system");
    }

    static OutputTarget createSystem(String system) {
        return getOrCreate("system");
    }
}
