package de.joshavg.yaircclient.bridge;

import de.joshavg.yaircclient.api.Client;
import de.joshavg.yaircclient.api.ResponseParser;
import de.joshavg.yaircclient.api.listener.ApiListener;
import de.joshavg.yaircclient.gui.ActionType;
import de.joshavg.yaircclient.gui.MainForm;
import de.joshavg.yaircclient.gui.OutputFactory;
import de.joshavg.yaircclient.gui.OutputTarget;
import java.util.Map;
import javax.inject.Inject;

public class ApiLogger implements ApiListener {

    private final MainForm form;
    private final OutputFactory outputFactory;

    @Inject
    public ApiLogger(MainForm form, OutputFactory outputFactory) {
        this.form = form;
        this.outputFactory = outputFactory;
    }

    @Override
    public void parsed(Map<ResponseParser.Key, ResponseParser.ResponseValue> parsed,
        Client client) {
        OutputTarget target = outputFactory.getSystem();

        String message = String.format("%1s: %2s",
            parsed.get(ResponseParser.Key.CMD),
            parsed.get(ResponseParser.Key.PAYLOAD));
        target.writeln(message, ActionType.NOTICE);

        if ("NICK".equals(parsed.get(ResponseParser.Key.CMD).get())) {
            nickChange(parsed);
        }
    }

    private void nickChange(Map<ResponseParser.Key, ResponseParser.ResponseValue> parsed) {
        String user = parsed.get(ResponseParser.Key.USER).get();
        String newNick = parsed.get(ResponseParser.Key.PAYLOAD).get();

        OutputTarget currentTarget = form.getCurrentTarget();
        String message = String.format("%s changed nick to %s", user, newNick);
        currentTarget.writeln(message, ActionType.NOTICE);
    }
}
