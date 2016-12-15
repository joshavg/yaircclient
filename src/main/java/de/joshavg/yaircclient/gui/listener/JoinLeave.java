package de.joshavg.yaircclient.gui.listener;

import de.joshavg.yaircclient.api.Client;
import de.joshavg.yaircclient.api.Message;
import de.joshavg.yaircclient.gui.GuiListener;
import de.joshavg.yaircclient.gui.MainForm;
import de.joshavg.yaircclient.gui.OutputFactory;
import de.joshavg.yaircclient.gui.OutputTarget;

public class JoinLeave implements GuiListener {
    private Client client;

    public JoinLeave(Client client) {
        this.client = client;
    }

    @Override
    public void messageTyped(String message, MainForm gui) {
        String[] split = message.split(" ");

        if (split[0].equals("/j")) {
            joinChannel(gui, split[1]);
        } else if (split[0].equals("/p")) {
            partChannel(gui, split);
        }
    }

    private void partChannel(MainForm gui, String[] split) {
        String channel = gui.getCurrentTarget().getTarget();
        if (split.length > 1) {
            channel = split[1];
        }

        client.write(Message.part(channel));
        gui.setActiveTarget(OutputFactory.getSystem());
        OutputFactory.remove(channel);
        OutputFactory.getSystem().writeln("parted from " + channel);
    }

    private void joinChannel(MainForm gui, String channel) {
        if (channel.startsWith("#")) {
            client.write(Message.join(channel));
        }

        OutputTarget target = OutputFactory.getOrCreate(channel);
        gui.setActiveTarget(target);
    }
}
