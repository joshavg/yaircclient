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

import static de.joshavg.yaircclient.api.ResponseParser.Key.*;

public class PartDisplay implements ApiListener {
    private final MainForm form;
    private final OutputFactory outputFactory;

    @Inject
    public PartDisplay(MainForm form, OutputFactory outputFactory) {
        this.form = form;
        this.outputFactory = outputFactory;
    }

    @Override
    public void parsed(Map<ResponseParser.Key, ResponseParser.ResponseValue> parsed, Client client) {
        String command = parsed.get(CMD).get();
        if ("PART".equals(command)) {
            handlePart(parsed);
        } else if ("QUIT".equals(command)) {
            handleQuit(parsed);
        }
    }

    private void handleQuit(Map<ResponseParser.Key, ResponseParser.ResponseValue> parsed) {
        String user = parsed.get(USER).get();
        String payload = parsed.get(PAYLOAD).get();
        String nick = parsed.get(NICK).get();

        String message = String.format("%s (%s) quit (%s)", nick, user, payload);
        form.getCurrentTarget().writeln(message, ActionType.CHANNEL);
    }

    private void handlePart(Map<ResponseParser.Key, ResponseParser.ResponseValue> parsed) {
        String nick = parsed.get(NICK).get();
        String channel = parsed.get(CHANNEL).get();
        String user = parsed.get(USER).get();

        String message = String.format("%s (%s) parted", nick, user);
        OutputTarget target = outputFactory.getOrCreate(channel);
        target.writeln(message, ActionType.CHANNEL);
    }
}
