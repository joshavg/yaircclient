package de.joshavg.yaircclient.gui;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class OutputFactory {

    private static final Map<String, OutputTarget> TARGETS = new HashMap<>();

    private OutputFactory() {
    }

    public static OutputTarget getOrCreate(String name) {
        if (TARGETS.containsKey(name)) {
            return TARGETS.get(name);
        }

        OutputTarget pane = new OutputTarget(name);
        TARGETS.put(name, pane);

        return pane;
    }

    public static OutputTarget get(String name) {
        return TARGETS.get(name);
    }

    public static OutputTarget getSystem() {
        return TARGETS.get("system");
    }

    static OutputTarget createSystem() {
        return getOrCreate("system");
    }

    public static Map<String, OutputTarget> getAll() {
        return Collections.unmodifiableMap(TARGETS);
    }

    public static void remove(String name) {
        TARGETS.remove(name);
    }
}
