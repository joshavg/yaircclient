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

        String from = parsed.get(ResponseParser.Key.TARGET).get();
        String sender = parsed.get(ResponseParser.Key.SENDER).get();
        String payload = parsed.get(ResponseParser.Key.PAYLOAD).get();

        ActionType type = ActionType.MESSAGE;
        if (payload.toLowerCase().contains(client.getNick().toLowerCase())) {
            type = ActionType.HIGHLIGHT;
            form.showNotification("highlight in " + from, payload);
        }
        displayMessage(OutputFactory.getOrCreate(from), sender, payload, type);
    }

    public void displayMessage(OutputTarget target, String sender, String message, ActionType type) {
        String line = String.format("%s - %s: %s",
                DATE_FORMAT.format(new Date()), sender, message);

        target.writeln(line, type);
    }
}
