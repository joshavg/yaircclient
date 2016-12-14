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

import static de.joshavg.yaircclient.gui.ActionType.MESSAGE;
import static de.joshavg.yaircclient.gui.ActionType.NOTICE;

public class MessageDisplay implements ApiListener {
    private static final DateFormat DATE_FORMAT = DateFormat.getTimeInstance(DateFormat.DEFAULT, Locale.getDefault());

    private final MainForm form;

    public MessageDisplay(MainForm form) {
        this.form = form;
    }

    @Override
    public void parsed(Map<ResponseParser.Key, ResponseParser.ResponseValue> parsed, Client client) {
        if (!parsed.get(ResponseParser.Key.CMD).get().equals("PRIVMSG")) {
            return;
        }

        String target = parsed.get(ResponseParser.Key.TARGET).get();
        String sender = parsed.get(ResponseParser.Key.SENDER).get();
        String payload = parsed.get(ResponseParser.Key.PAYLOAD).get();
        String respondTo = parsed.get(ResponseParser.Key.RESPOND_TO).get();

        boolean notified = notify(client, target, payload);

        ActionType type = MESSAGE;
        if (notified) {
            type = NOTICE;
        }

        displayMessage(OutputFactory.getOrCreate(respondTo), sender, payload, type);
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
