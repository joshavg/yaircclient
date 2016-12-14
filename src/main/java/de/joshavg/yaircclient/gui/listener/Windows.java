package de.joshavg.yaircclient.gui.listener;

import de.joshavg.yaircclient.bridge.MessageReadStatus;
import de.joshavg.yaircclient.gui.*;

public class Windows implements GuiListener {

    private final MainForm form;
    private final MessageReadStatus readStatus;

    public Windows(MainForm form, MessageReadStatus readStatus) {
        this.form = form;
        this.readStatus = readStatus;
    }

    @Override
    public void messageTyped(String message, MainForm gui) {
        String[] split = message.split("\\s");

        if (!split[0].equals("/w")) {
            return;
        }

        if (split.length == 1 || split[1].equals("l")) {
            listWindows();
        } else if (split.length == 2) {
            setActiveWindow(split[1]);
        }
    }

    private void setActiveWindow(String name) {
        OutputTarget target = OutputFactory.get(name);
        if (target != null) {
            form.setActiveTarget(target);
            target.jumpToEnd();
        } else {
            form.getCurrentTarget().writeln("unknown window: \"" + name + "\"", ActionType.ERROR);
        }
    }

    private void listWindows() {
        OutputTarget currentTarget = form.getCurrentTarget();
        OutputFactory.getAll().keySet().forEach(target -> {
            boolean hasUnread = readStatus.readStatus(target);
            currentTarget.writeln(target + (hasUnread ? "!" : ""));
        });
    }
}
