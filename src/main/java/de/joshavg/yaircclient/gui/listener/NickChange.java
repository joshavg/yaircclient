package de.joshavg.yaircclient.gui.listener;

import de.joshavg.yaircclient.api.Client;
import de.joshavg.yaircclient.api.Message;
import de.joshavg.yaircclient.gui.GuiListener;
import de.joshavg.yaircclient.gui.MainForm;

public class NickChange implements GuiListener {
    private final Client client;

    public NickChange(Client client) {
        this.client = client;
    }

    @Override
    public void messageTyped(String message, MainForm gui) {
        String[] split = message.split(" ");
        if (!"/n".equals(split[0])) {
            return;
        }

        String newNick = split[1];
        client.setNick(newNick);
        client.write(Message.nick(newNick));
    }
}
