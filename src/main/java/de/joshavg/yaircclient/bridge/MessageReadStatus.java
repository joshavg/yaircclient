package de.joshavg.yaircclient.bridge;

import de.joshavg.yaircclient.api.Client;
import de.joshavg.yaircclient.api.ResponseParser;
import de.joshavg.yaircclient.api.listener.ApiListener;
import de.joshavg.yaircclient.gui.GuiListener;
import de.joshavg.yaircclient.gui.MainForm;
import de.joshavg.yaircclient.gui.OutputTarget;

import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class MessageReadStatus implements ApiListener, GuiListener {
    private final MainForm form;

    private final Map<String, Boolean> unreadMessages;

    @Inject
    public MessageReadStatus(MainForm form) {
        this.form = form;
        unreadMessages = new HashMap<>();
    }

    @Override
    public void parsed(Map<ResponseParser.Key, ResponseParser.ResponseValue> parsed, Client client) {
        if (!"PRIVMSG".equals(parsed.get(ResponseParser.Key.CMD).get())) {
            return;
        }

        String target = parsed.get(ResponseParser.Key.RESPOND_TO).get();
        String currentTarget = form.getCurrentTarget().getTarget();

        if (!target.equals(currentTarget)) {
            unreadMessages.put(target, true);
            form.showReadIndicator(true);
        }
    }

    @Override
    public void targetChanged(OutputTarget oldTarget, MainForm gui) {
        unreadMessages.put(gui.getCurrentTarget().getTarget(), false);

        boolean unreadWindowExists = unreadMessages.values().stream().anyMatch(r -> r);
        form.showReadIndicator(unreadWindowExists);
    }

    public boolean readStatus(String target) {
        return unreadMessages.getOrDefault(target, false);
    }
}
