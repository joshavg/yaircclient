package de.joshavg.yaircclient.gui;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Singleton;

@Singleton
public class OutputFactory {

    private static final String SYSTEM_KEY = "system";

    private final Map<String, OutputTarget> targets = new HashMap<>();

    OutputFactory() {
        createSystem();
    }

    public OutputTarget getOrCreate(String name) {
        if (targets.containsKey(name)) {
            return targets.get(name);
        }

        OutputTarget pane = new OutputTarget(name);
        targets.put(name, pane);

        return pane;
    }

    public OutputTarget get(String name) {
        return targets.get(name);
    }

    public OutputTarget getSystem() {
        return targets.get(SYSTEM_KEY);
    }

    private void createSystem() {
        targets.put(SYSTEM_KEY, new OutputTarget(SYSTEM_KEY));
    }

    public Map<String, OutputTarget> getAll() {
        return Collections.unmodifiableMap(targets);
    }

    public void remove(String name) {
        targets.remove(name);
    }
}
