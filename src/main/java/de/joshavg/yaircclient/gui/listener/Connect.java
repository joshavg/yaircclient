package de.joshavg.yaircclient.gui.listener;

import com.eclipsesource.json.JsonObject;
import de.joshavg.yaircclient.Settings;
import de.joshavg.yaircclient.api.Client;
import de.joshavg.yaircclient.gui.ActionType;
import de.joshavg.yaircclient.gui.GuiListener;
import de.joshavg.yaircclient.gui.MainForm;
import de.joshavg.yaircclient.gui.OutputFactory;

public class Connect implements GuiListener {

    private Client client;

    public Connect(Client client) {
        this.client = client;
    }

    public static void connect(Client client) {
        JsonObject cfg = Settings.read();

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
            connect(client);
        } else {
            OutputFactory.getSystem().writeln("already connected", ActionType.ERROR);
        }
    }
}
