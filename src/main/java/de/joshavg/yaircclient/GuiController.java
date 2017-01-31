package de.joshavg.yaircclient;

import com.eclipsesource.json.JsonObject;
import de.joshavg.yaircclient.api.Client;
import de.joshavg.yaircclient.gui.MainForm;
import de.joshavg.yaircclient.gui.listener.*;
import javax.inject.Inject;

class GuiController {

    private final MainForm form;
    private final Client client;

    @Inject
    GuiController(MainForm form, Client client) {
        this.form = form;
        this.client = client;
    }

    void start(Brabbel brabbel) {
        form.addListener(brabbel.connect());
        form.addListener(brabbel.settingsListener());
        form.addListener(brabbel.exit());
        form.addListener(brabbel.windows());
        form.addListener(brabbel.joinLeave());
        form.addListener(brabbel.messageSend());
        form.addListener(brabbel.messageReadStatus());
        form.addListener(brabbel.nickChange());
        form.addListener(brabbel.messageHistory());
        form.addListener(brabbel.usersDisplay());
        form.addListener(brabbel.nickAutocomplete());

        client.addListener(brabbel.apiLogger());
        client.addListener(brabbel.messageDisplay());
        client.addListener(brabbel.joinDisplay());
        client.addListener(brabbel.partDisplay());
        client.addListener(brabbel.messageReadStatus());
        client.addListener(brabbel.usersDisplay());

        JsonObject cfg = brabbel.settings().read();
        boolean autojoin = cfg.getBoolean("autojoin", false);
        if (autojoin) {
            client.addListener(brabbel.autoJoin());
        }

        form.setVisible(true);

        boolean autoconnect = cfg.getBoolean("autoconnect", false);
        if (autoconnect) {
            Connect.connect(brabbel.client(), brabbel.settings());
        }
    }
}
