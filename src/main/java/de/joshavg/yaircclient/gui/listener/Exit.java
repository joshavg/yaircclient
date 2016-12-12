package de.joshavg.yaircclient.gui.listener;

import de.joshavg.yaircclient.api.Client;
import de.joshavg.yaircclient.gui.ActionType;
import de.joshavg.yaircclient.gui.GuiListener;
import de.joshavg.yaircclient.gui.MainForm;
import de.joshavg.yaircclient.gui.OutputFactory;

public class Exit implements GuiListener {

    private boolean confirmWarned;
    private Client client;

    public Exit(Client client) {
        this.client = client;
    }

    @Override
    public void messageTyped(String message, MainForm gui) {
        if (message.equals("/e")) {
            if (!confirmWarned) {
                gui.getCurrentTarget().writeln("type again to confirm", ActionType.ERROR);
                confirmWarned = true;
            } else {
                quit(gui);
            }
        } else if (message.equals("/e!")) {
            quit(gui);
        } else {
            confirmWarned = false;
        }
    }

    private void quit(MainForm gui) {
        client.quit();
        gui.dispose();
        System.exit(0);
    }
}
