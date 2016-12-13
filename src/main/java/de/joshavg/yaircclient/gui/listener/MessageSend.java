package de.joshavg.yaircclient.gui.listener;

import de.joshavg.yaircclient.api.Client;
import de.joshavg.yaircclient.api.Message;
import de.joshavg.yaircclient.bridge.MessageDisplay;
import de.joshavg.yaircclient.gui.GuiListener;
import de.joshavg.yaircclient.gui.MainForm;

public class MessageSend implements GuiListener {
    private final Client client;
    private final MessageDisplay display;

    public MessageSend(Client client, MessageDisplay display) {
        this.client = client;
        this.display = display;
    }

    @Override
    public void messageTyped(String message, MainForm gui) {
        if (message.startsWith("/")) {
            return;
        }

        String target = gui.getCurrentTarget().getTarget();
        client.write(Message.privmsg(target, message));

        display.displayMessage(target, client.getNick(), message);
    }
}
