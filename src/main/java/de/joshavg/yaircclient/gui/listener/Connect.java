package de.joshavg.yaircclient.gui.listener;

import com.eclipsesource.json.JsonObject;
import de.joshavg.yaircclient.Settings;
import de.joshavg.yaircclient.api.Client;
import de.joshavg.yaircclient.gui.ActionType;
import de.joshavg.yaircclient.gui.GuiListener;
import de.joshavg.yaircclient.gui.MainForm;
import de.joshavg.yaircclient.gui.OutputFactory;
import javax.inject.Inject;

public class Connect implements GuiListener {

    private final Client client;
    private final Settings settings;
    private final OutputFactory outputFactory;

    @Inject
    public Connect(Client client, Settings settings, OutputFactory outputFactory) {
        this.client = client;
        this.settings = settings;
        this.outputFactory = outputFactory;
    }

    public static void connect(Client client, Settings settings) {
        JsonObject cfg = settings.read();

        JsonObject cx = cfg.get("connection").asObject();
        String url = cx.getString("url", "empty_url");
        int port = cx.getInt("port", 6667);
        String nick = cfg.getString("nick", "empty_nick");

        client.connect(url, nick, port);
    }

    @Override
    public void messageTyped(String message, MainForm gui) {
        if (!"/c".equals(message)) {
            return;
        }

        if (!client.isConnected()) {
            connect(client, settings);
        } else {
            outputFactory.getSystem().writeln("already connected", ActionType.ERROR);
        }
    }
}
