package de.joshavg.yaircclient.gui.listener;

import static de.joshavg.yaircclient.gui.ActionType.ERROR;

import de.joshavg.yaircclient.bridge.MessageReadStatus;
import de.joshavg.yaircclient.gui.ActionType;
import de.joshavg.yaircclient.gui.GuiListener;
import de.joshavg.yaircclient.gui.MainForm;
import de.joshavg.yaircclient.gui.OutputFactory;
import de.joshavg.yaircclient.gui.OutputTarget;
import java.util.List;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.swing.JTextField;

public class Windows implements GuiListener {

    private final MainForm form;
    private final MessageReadStatus readStatus;
    private final OutputFactory outputFactory;

    @Inject
    public Windows(MainForm form, MessageReadStatus readStatus, OutputFactory outputFactory) {
        this.form = form;
        this.readStatus = readStatus;
        this.outputFactory = outputFactory;
    }

    private boolean affectsMe(String text) {
        return text.startsWith("/w ") || "/w".equals(text);
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
            List<String> outputs = outputFactory
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
        if (window.equals(outputFactory.getSystem().getTarget())) {
            form.getCurrentTarget().writeln("cannot remove the system window", ERROR);
        } else if (window.equals(form.getCurrentTarget().getTarget())) {
            form.getCurrentTarget().writeln("cannot remove the current window", ERROR);
        } else {
            outputFactory.remove(window);
        }
    }

    private void setActiveWindow(String name) {
        OutputTarget target = outputFactory.get(name);
        if (target != null) {
            form.setActiveTarget(target);
            target.jumpToEnd();
        } else {
            form.getCurrentTarget().writeln("unknown window: \"" + name + "\"", ERROR);
        }
    }

    private void listWindows() {
        OutputTarget currentTarget = form.getCurrentTarget();
        outputFactory.getAll().keySet().forEach(target -> {
            boolean hasUnread = readStatus.readStatus(target);
            currentTarget.writeln(target + (hasUnread ? "!" : ""));
        });
    }
}
