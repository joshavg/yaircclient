package de.joshavg.yaircclient.bridge;

import de.joshavg.yaircclient.api.listener.UsersCollector;
import de.joshavg.yaircclient.gui.GuiListener;
import de.joshavg.yaircclient.gui.MainForm;
import de.joshavg.yaircclient.gui.OutputTarget;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.swing.JTextField;

@Singleton
public class NickAutocomplete implements GuiListener {

    private final UsersCollector usersCollector;

    @Inject
    NickAutocomplete(UsersCollector usersCollector) {
        this.usersCollector = usersCollector;
    }

    @Override
    public void tabPressed(MainForm gui, JTextField field) {
        OutputTarget currentTarget = gui.getCurrentTarget();
        String channel = currentTarget.getTarget();

        if (!channel.startsWith("#")) {
            return;
        }

        usersCollector.requestUsersInChannel(channel, us -> {
            String text = field.getText();
            String[] parts = text.split("\\s");
            String search = parts[parts.length - 1];

            List<String> users = us.stream()
                .map(u -> u.replaceFirst("[~&@%+]", ""))
                .filter(u -> u.startsWith(search))
                .collect(Collectors.toList());

            if (users.size() == 1) {
                String user = users.get(0);
                String firstPart = String
                    .join(" ", Arrays.asList(parts).subList(0, parts.length - 1));
                field.setText(firstPart + " " + user);
                field.setCaretPosition(field.getText().length());
            }
        });
    }
}
