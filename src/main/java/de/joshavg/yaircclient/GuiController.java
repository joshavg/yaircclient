package de.joshavg.yaircclient;

import com.eclipsesource.json.JsonObject;
import de.joshavg.yaircclient.api.Client;
import de.joshavg.yaircclient.api.ClientFactory;
import de.joshavg.yaircclient.bridge.ApiLogger;
import de.joshavg.yaircclient.gui.MainForm;
import de.joshavg.yaircclient.gui.listener.Connect;
import de.joshavg.yaircclient.gui.listener.Exit;
import de.joshavg.yaircclient.gui.listener.SettingsListener;

class GuiController {

    private final Client client;
    private MainForm form;

    GuiController() {
        this.form = new MainForm();
        this.client = ClientFactory.getClient();
    }

    void start() {
        form.addListener(new Connect(client));
        form.addListener(new SettingsListener());
        form.addListener(new Exit(client));

        client.addListener(new ApiLogger());

        form.setVisible(true);

        JsonObject cfg = Settings.read();
        boolean autoconnect = cfg.getBoolean("autoconnect", false);
        if (autoconnect) {
            Connect.connect(client);
        }
    }
}
