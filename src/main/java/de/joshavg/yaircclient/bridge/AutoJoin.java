package de.joshavg.yaircclient.bridge;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonValue;
import de.joshavg.yaircclient.Settings;
import de.joshavg.yaircclient.api.Client;
import de.joshavg.yaircclient.api.Message;
import de.joshavg.yaircclient.api.listener.ApiListener;
import de.joshavg.yaircclient.gui.OutputFactory;
import javax.inject.Inject;

public class AutoJoin implements ApiListener {
    private final Settings settings;
    private final OutputFactory outputFactory;

    @Inject
    public AutoJoin(Settings settings, OutputFactory outputFactory) {
      this.settings = settings;
      this.outputFactory = outputFactory;
    }

    @Override
    public void connected(Client client) {
        JsonArray channels = settings.read().get("channels").asArray();
        channels.values().stream()
                .map(JsonValue::asString)
                .forEach(c -> {
                    outputFactory.getSystem().writeln("autojoining channel " + c);
                    client.write(Message.join(c));
                });
    }
}
