package de.joshavg.yaircclient.gui.listener;

import de.joshavg.yaircclient.gui.*;

public class Windows implements GuiListener {

    private final MainForm form;

    public Windows(MainForm form) {
        this.form = form;
    }

    @Override
    public void messageTyped(String message, MainForm gui) {
        String[] split = message.split("\\s");

        if (!split[0].equals("/w")) {
            return;
        }

        if (split.length == 1 || split[1].equals("l")) {
            listWindows();
        } else if (split[1].equals("v") && split.length == 3) {
            setActiveWindow(split[2]);
        }
    }

    private void setActiveWindow(String name) {
        OutputTarget target = OutputFactory.get(name);
        if (target != null) {
            form.setActiveTarget(target);
        } else {
            form.getCurrentTarget().writeln("unknown window: \"" + name + "\"", ActionType.ERROR);
        }
    }

    private void listWindows() {
        OutputTarget currentTarget = form.getCurrentTarget();
        OutputFactory.getAll().keySet().forEach(currentTarget::writeln);
    }
}
