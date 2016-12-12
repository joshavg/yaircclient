package de.joshavg.yaircclient.bridge;

import de.joshavg.yaircclient.api.Client;
import de.joshavg.yaircclient.api.ResponseParser;
import de.joshavg.yaircclient.api.listener.ApiListener;
import de.joshavg.yaircclient.gui.MainForm;
import de.joshavg.yaircclient.gui.OutputFactory;
import de.joshavg.yaircclient.gui.OutputTarget;

import java.util.Map;

public class ResponseGuiResult implements ApiListener {

    private final MainForm form;

    public ResponseGuiResult(MainForm form) {
        this.form = form;
    }

    @Override
    public void parsed(Map<ResponseParser.Key, ResponseParser.ResponseValue> parsed, Client client) {
        if (!parsed.get(ResponseParser.Key.CMD).get().equals("JOIN")) {
            return;
        }

        String channel = parsed.get(ResponseParser.Key.TARGET).get();
        OutputTarget target = OutputFactory.getOrCreate(channel);
        form.setActiveTarget(target);
    }
}