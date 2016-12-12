package de.joshavg.yaircclient.gui.listener;

import de.joshavg.yaircclient.gui.GuiListener;
import de.joshavg.yaircclient.gui.MainForm;
import de.joshavg.yaircclient.gui.OutputFactory;
import de.joshavg.yaircclient.gui.OutputTarget;

public class Windows implements GuiListener {

    private final MainForm form;

    public Windows(MainForm form) {
        this.form = form;
    }

    @Override
    public void messageTyped(String message, MainForm gui) {
        if (!message.startsWith("/w")) {
            return;
        }

        String[] action = message.split("\\s");

        if (action.length == 1 || action[1].equals("l")) {
            OutputTarget currentTarget = form.getCurrentTarget();
            OutputFactory.getAll().keySet().forEach(currentTarget::writeln);
        } else if (action[1].equals("v") && action.length == 3) {
            OutputTarget target = OutputFactory.getOrCreate(action[2]);
            form.setActiveTarget(target);
        }
    }
}
