package de.joshavg.yaircclient.bridge;

import de.joshavg.yaircclient.api.Client;
import de.joshavg.yaircclient.api.ResponseParser;
import de.joshavg.yaircclient.api.listener.ApiListener;
import de.joshavg.yaircclient.gui.ActionType;
import de.joshavg.yaircclient.gui.MainForm;
import de.joshavg.yaircclient.gui.OutputFactory;
import de.joshavg.yaircclient.gui.OutputTarget;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import javax.inject.Inject;

import static de.joshavg.yaircclient.gui.ActionType.HIGHLIGHT;
import static de.joshavg.yaircclient.gui.ActionType.MESSAGE;

public class MessageDisplay implements ApiListener {
    private static final DateFormat DATE_FORMAT = DateFormat.getTimeInstance(DateFormat.DEFAULT, Locale.getDefault());

    private final MainForm form;
    private final OutputFactory outputFactory;

    @Inject
    public MessageDisplay(MainForm form, OutputFactory outputFactory) {
        this.form = form;
        this.outputFactory = outputFactory;
    }

    @Override
    public void parsed(Map<ResponseParser.Key, ResponseParser.ResponseValue> parsed, Client client) {
        if (!"PRIVMSG".equals(parsed.get(ResponseParser.Key.CMD).get())) {
            return;
        }

        String target = parsed.get(ResponseParser.Key.TARGET).get();
        String sender = parsed.get(ResponseParser.Key.SENDER).get();
        String payload = parsed.get(ResponseParser.Key.PAYLOAD).get();
        String respondTo = parsed.get(ResponseParser.Key.RESPOND_TO).get();

        boolean notified = notify(client, target, payload);

        ActionType type = MESSAGE;
        if (notified) {
            type = HIGHLIGHT;
        }

        displayMessage(outputFactory.getOrCreate(respondTo), sender, payload, type);
    }

    private boolean notify(Client client, String target, String payload) {
        boolean containsNick = payload.toLowerCase().contains(client.getNick().toLowerCase());

        if (containsNick && target.startsWith("#")) {
            form.showNotification("Highlight in channel " + target, payload);
            return true;
        } else if (!target.startsWith("#")) {
            form.showNotification("New private message", "from " + target);
            return true;
        }

        return false;
    }

    public void displayMessage(OutputTarget target, String sender, String message, ActionType type) {
        String line = String.format("%s - %s: %s",
                DATE_FORMAT.format(new Date()), sender, message);

        target.writeln(line, type);
    }
}
