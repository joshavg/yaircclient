package de.joshavg.yaircclient.gui.listener;

import de.joshavg.yaircclient.api.Client;
import de.joshavg.yaircclient.api.Message;
import de.joshavg.yaircclient.gui.GuiListener;
import de.joshavg.yaircclient.gui.MainForm;

public class ChannelJoinLeave implements GuiListener {
    private Client client;

    public ChannelJoinLeave(Client client) {
        this.client = client;
    }

    @Override
    public void messageTyped(String message, MainForm gui) {
        if (!message.startsWith("/j")) {
            return;
        }

        String channel = message.split("\\s")[1];
        client.write(Message.join(channel));
    }
}
