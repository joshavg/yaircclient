package de.joshavg.yaircclient.gui.listener;

import de.joshavg.yaircclient.api.Client;
import de.joshavg.yaircclient.api.Message;
import de.joshavg.yaircclient.gui.GuiListener;
import de.joshavg.yaircclient.gui.MainForm;
import de.joshavg.yaircclient.gui.OutputFactory;
import de.joshavg.yaircclient.gui.OutputTarget;
import javax.inject.Inject;

public class JoinLeave implements GuiListener {

    private final Client client;
    private final OutputFactory outputFactory;

    @Inject
    public JoinLeave(Client client, OutputFactory outputFactory) {
        this.client = client;
        this.outputFactory = outputFactory;
    }

    @Override
    public void messageTyped(String message, MainForm gui) {
        String[] split = message.split(" ");

        if ("/j".equals(split[0])) {
            joinChannel(gui, split[1]);
        } else if ("/p".equals(split[0])) {
            partChannel(gui, split);
        }
    }

    private void partChannel(MainForm gui, String[] split) {
        String channel = gui.getCurrentTarget().getTarget();
        if (split.length > 1) {
            channel = split[1];
        }

        client.write(Message.part(channel));
        gui.setActiveTarget(outputFactory.getSystem());
        outputFactory.remove(channel);
        outputFactory.getSystem().writeln("parted from " + channel);
    }

    private void joinChannel(MainForm gui, String channel) {
        if (channel.startsWith("#")) {
            client.write(Message.join(channel));
        }

        OutputTarget target = outputFactory.getOrCreate(channel);
        gui.setActiveTarget(target);
    }
}
