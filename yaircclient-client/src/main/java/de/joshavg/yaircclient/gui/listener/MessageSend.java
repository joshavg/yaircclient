package de.joshavg.yaircclient.gui.listener;

import static de.joshavg.yaircclient.gui.ActionType.NOTICE;

import de.joshavg.yaircclient.api.Client;
import de.joshavg.yaircclient.api.Message;
import de.joshavg.yaircclient.bridge.MessageDisplay;
import de.joshavg.yaircclient.gui.GuiListener;
import de.joshavg.yaircclient.gui.MainForm;
import de.joshavg.yaircclient.gui.OutputFactory;
import javax.inject.Inject;

public class MessageSend implements GuiListener {

    private final Client client;
    private final MessageDisplay display;
    private final OutputFactory outputFactory;

    @Inject
    public MessageSend(Client client, MessageDisplay display, OutputFactory outputFactory) {
        this.client = client;
        this.display = display;
        this.outputFactory = outputFactory;
    }

    @Override
    public void messageTyped(String message, MainForm gui) {
        if (message.startsWith("/")) {
            return;
        }

        String target = gui.getCurrentTarget().getTarget();
        client.write(Message.privmsg(target, message));

        display
            .displayMessage(outputFactory.getOrCreate(target), client.getNick(), message, NOTICE);
    }
}
