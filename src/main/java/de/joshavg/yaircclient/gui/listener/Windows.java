package de.joshavg.yaircclient.gui.listener;

import de.joshavg.yaircclient.bridge.MessageReadStatus;
import de.joshavg.yaircclient.gui.*;

import javax.swing.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static de.joshavg.yaircclient.gui.ActionType.ERROR;

public class Windows implements GuiListener {

    private final MainForm form;
    private final MessageReadStatus readStatus;

    public Windows(MainForm form, MessageReadStatus readStatus) {
        this.form = form;
        this.readStatus = readStatus;
    }

    private boolean affectsMe(String text) {
        return text.startsWith("/w ") || text.equals("/w");
    }

    @Override
    public void messageTyped(String message, MainForm gui) {
        if (!affectsMe(message)) {
            return;
        }

        String[] split = message.split("\\s");

        if (split.length == 1) {
            listWindows();
        } else if (split.length == 2) {
            setActiveWindow(split[1]);
        } else if (split.length == 3 && "r".equals(split[1])) {
            String window = split[2];
            removeWindow(window);
        }
    }

    @Override
    public void tabPressed(MainForm gui, JTextField field) {
        String text = field.getText();

        if (!affectsMe(text)) {
            return;
        }

        String[] split = text.split("\\s");
        if (split.length == 2) {
            String search = split[1];
            List<String> outputs = OutputFactory
                    .getAll()
                    .keySet()
                    .stream()
                    .filter(on -> on.startsWith(search))
                    .collect(Collectors.toList());
            if (outputs.size() == 1) {
                field.setText("/w " + outputs.get(0));
            } else {
                outputs.forEach(on -> gui.getCurrentTarget().writeln(on, ActionType.NOTICE));
            }
        }
    }

    private void removeWindow(String window) {
        if (window.equals(OutputFactory.getSystem().getTarget())) {
            form.getCurrentTarget().writeln("cannot remove the system window", ERROR);
        } else if (window.equals(form.getCurrentTarget().getTarget())) {
            form.getCurrentTarget().writeln("cannot remove the current window", ERROR);
        } else {
            OutputFactory.remove(window);
        }
    }

    private void setActiveWindow(String name) {
        OutputTarget target = OutputFactory.get(name);
        if (target != null) {
            form.setActiveTarget(target);
            target.jumpToEnd();
        } else {
            form.getCurrentTarget().writeln("unknown window: \"" + name + "\"", ERROR);
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
