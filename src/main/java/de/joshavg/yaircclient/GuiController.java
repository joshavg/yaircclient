package de.joshavg.yaircclient;

import com.eclipsesource.json.JsonObject;
import de.joshavg.yaircclient.api.Client;
import de.joshavg.yaircclient.api.ClientFactory;
import de.joshavg.yaircclient.bridge.*;
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
        MessageReadStatus messageReadStatus = new MessageReadStatus(form);

        form.addListener(new Connect(client));
        form.addListener(new SettingsListener());
        form.addListener(new Exit(client));
        form.addListener(new Windows(form, messageReadStatus));
        form.addListener(new JoinLeave(client));
        form.addListener(new MessageSend(client, messageDisplay));
        form.addListener(messageReadStatus);
        form.addListener(new NickChange(client));

        client.addListener(new ApiLogger(form));
        client.addListener(messageDisplay);
        client.addListener(new JoinDisplay());
        client.addListener(new PartDisplay());
        client.addListener(messageReadStatus);

        JsonObject cfg = Settings.read();
        boolean autojoin = cfg.getBoolean("autojoin", false);
        if (autojoin) {
            client.addListener(new AutoJoin());
        }

        form.setVisible(true);

        boolean autoconnect = cfg.getBoolean("autoconnect", false);
        if (autoconnect) {
            Connect.connect(client);
        }
    }
}
