package de.joshavg.yaircclient.gui.listener;

import de.joshavg.yaircclient.api.Client;
import de.joshavg.yaircclient.api.Message;
import de.joshavg.yaircclient.gui.GuiListener;
import de.joshavg.yaircclient.gui.MainForm;
import de.joshavg.yaircclient.gui.OutputFactory;
import de.joshavg.yaircclient.gui.OutputTarget;

public class ChannelJoinLeave implements GuiListener {
    private Client client;

    public ChannelJoinLeave(Client client) {
        this.client = client;
    }

    @Override
    public void messageTyped(String message, MainForm gui) {
        if (message.startsWith("/j")) {
            String channel = message.split("\\s")[1];
            client.write(Message.join(channel));

            OutputTarget target = OutputFactory.getOrCreate(channel);
            gui.setActiveTarget(target);
        } else if (message.startsWith("/p")) {
            String[] split = message.split(" ");

            String channel = gui.getCurrentTarget().getTarget();
            if (split.length > 1) {
                channel = split[1];
            }

            client.write(Message.part(channel));
            gui.setActiveTarget(OutputFactory.getSystem());
            OutputFactory.remove(channel);
            OutputFactory.getSystem().writeln("parted from " + channel);
        }
    }
}
