package de.joshavg.yaircclient;

import com.eclipsesource.json.JsonObject;
import de.joshavg.yaircclient.api.Client;
import de.joshavg.yaircclient.api.ClientFactory;
import de.joshavg.yaircclient.bridge.ApiLogger;
import de.joshavg.yaircclient.bridge.JoinDisplay;
import de.joshavg.yaircclient.bridge.MessageDisplay;
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
        MessageDisplay messageDisplay = new MessageDisplay(form);

        form.addListener(new Connect(client));
        form.addListener(new SettingsListener());
        form.addListener(new Exit(client));
        form.addListener(new Windows(form));
        form.addListener(new ChannelJoinLeave(client));
        form.addListener(new MessageSend(client, messageDisplay));

        client.addListener(new ApiLogger(form));
        client.addListener(messageDisplay);
        client.addListener(new JoinDisplay());

        form.setVisible(true);

        JsonObject cfg = Settings.read();
        boolean autoconnect = cfg.getBoolean("autoconnect", false);
        if (autoconnect) {
            Connect.connect(client);
        }
    }
}
