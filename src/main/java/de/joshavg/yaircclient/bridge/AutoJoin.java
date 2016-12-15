package de.joshavg.yaircclient.bridge;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonValue;
import de.joshavg.yaircclient.Settings;
import de.joshavg.yaircclient.api.Client;
import de.joshavg.yaircclient.api.Message;
import de.joshavg.yaircclient.api.listener.ApiListener;
import de.joshavg.yaircclient.gui.OutputFactory;

public class AutoJoin implements ApiListener {
    @Override
    public void connected(Client client) {
        JsonArray channels = Settings.read().get("channels").asArray();
        channels.values().stream()
                .map(JsonValue::asString)
                .forEach(c -> {
                    OutputFactory.getSystem().writeln("autojoining channel " + c);
                    client.write(Message.join(c));
                });
    }
}
