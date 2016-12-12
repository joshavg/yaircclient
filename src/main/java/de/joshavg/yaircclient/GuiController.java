package de.joshavg.yaircclient;

import com.eclipsesource.json.JsonObject;
import de.joshavg.yaircclient.api.Client;
import de.joshavg.yaircclient.api.ClientFactory;
import de.joshavg.yaircclient.bridge.ApiLogger;
import de.joshavg.yaircclient.bridge.ResponseGuiResult;
import de.joshavg.yaircclient.gui.MainForm;
import de.joshavg.yaircclient.gui.listener.*;

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
        form.addListener(new Windows(form));
        form.addListener(new ChannelJoinLeave(client));

        client.addListener(new ApiLogger());
        client.addListener(new ResponseGuiResult(form));

        form.setVisible(true);

        JsonObject cfg = Settings.read();
        boolean autoconnect = cfg.getBoolean("autoconnect", false);
        if (autoconnect) {
            Connect.connect(client);
        }
    }
}
