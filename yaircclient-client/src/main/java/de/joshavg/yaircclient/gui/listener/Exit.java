package de.joshavg.yaircclient.gui.listener;

import de.joshavg.yaircclient.api.Client;
import de.joshavg.yaircclient.gui.ActionType;
import de.joshavg.yaircclient.gui.GuiListener;
import de.joshavg.yaircclient.gui.MainForm;
import javax.inject.Inject;

public class Exit implements GuiListener {

    private boolean confirmWarned;
    private Client client;

    @Inject
    public Exit(Client client) {
        this.client = client;
    }

    @Override
    public void messageTyped(String message, MainForm gui) {
        switch (message) {
            case "/e":
                handleExitCall(gui);
                break;
            case "/e!":
                quit(gui);
                break;
            default:
                confirmWarned = false;
                break;
        }
    }

    private void handleExitCall(MainForm gui) {
        if (!confirmWarned) {
            gui.getCurrentTarget().writeln("type again to confirm", ActionType.ERROR);
            confirmWarned = true;
        } else {
            quit(gui);
        }
    }

    private void quit(MainForm gui) {
        client.quit();
        gui.dispose();
        System.exit(0);
    }
}
