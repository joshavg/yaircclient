package de.joshavg.yaircclient.gui.listener;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
import com.eclipsesource.json.WriterConfig;
import de.joshavg.yaircclient.Settings;
import de.joshavg.yaircclient.gui.*;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SettingsListener implements GuiListener {

    private static final Pattern pattern = Pattern.compile("/s\\s?([a-z])?\\s?([^\\s]+)?\\s?([^\\s]+)?");
    private static final String SETTING_AUTOCONNECT = "autoconnect";
    private static final String SETTING_AUTOJOIN = "autojoin";

    @Override
    public void messageTyped(String message, MainForm gui) {
        Matcher matcher = pattern.matcher(message);
        if (!matcher.matches()) {
            return;
        }

        String grp1 = matcher.group(1);
        switch (Optional.ofNullable(grp1).orElse("")) {
            case "l":
                listSettings();
                break;
            case "d":
                displaySettings();
                break;
            case "s":
                setSetting(matcher.group(2), matcher.group(3));
                break;
            case "a":
                addChannel(matcher.group(2));
                break;
            case "r":
                removeChannel(matcher.group(2));
                break;
            default:
                listOrders();
        }
    }

    private void addChannel(String channel) {
        JsonObject cfg = Settings.read();
        JsonArray channels = cfg.get("channels").asArray();

        channels.add(channel);

        Settings.write(cfg);
    }

    private void removeChannel(String channel) {
        JsonObject cfg = Settings.read();
        JsonArray channels = cfg.get("channels").asArray();

        int i = 0;
        for (JsonValue cfgchannel : channels.values()) {
            if (cfgchannel.asString().equals(channel)) {
                channels.remove(i);
                break;
            }
            i++;
        }

        Settings.write(cfg);
    }

    private void setSetting(String name, String value) {
        JsonObject cfg = Settings.read();
        JsonObject cx = cfg.get("connection").asObject();

        switch (name) {
            case "url":
                cx.set("url", value);
                break;
            case "port":
                cx.set("port", Integer.parseInt(value));
                break;
            case "nick":
                cfg.set("nick", value);
                break;
            case SETTING_AUTOCONNECT:
                cfg.set(SETTING_AUTOCONNECT, Boolean.valueOf(value));
                break;
            case SETTING_AUTOJOIN:
                cfg.set(SETTING_AUTOJOIN, Boolean.valueOf(value));
                break;
            default:
                OutputFactory.getSystem().writeln("unknown setting " + name, ActionType.ERROR);
        }

        Settings.write(cfg);
    }

    private void displaySettings() {
        OutputTarget target = OutputFactory.getSystem();
        target.writeln(Settings.read().toString(WriterConfig.PRETTY_PRINT));
    }

    private void listOrders() {
        OutputTarget target = OutputFactory.getSystem();
        target.writeln("l: list settings    - /s l");
        target.writeln("d: display settings - /s d");
        target.writeln("s: set setting      - /s s [name] [value]");
        target.writeln("a: add channel      - /s a [name]");
        target.writeln("r: remove channel   - /s r [name]");
    }

    private void listSettings() {
        JsonObject cfg = Settings.read();
        JsonObject cx = cfg.get("connection").asObject();

        OutputTarget target = OutputFactory.getSystem();
        target.writeln("url        : " + cx.get("url"));
        target.writeln("port       : " + cx.get("port"));
        target.writeln("nick       : " + cfg.get("nick"));
        target.writeln("autoconnect: " + cfg.get(SETTING_AUTOCONNECT));
        target.writeln("autojoin   : " + cfg.get(SETTING_AUTOJOIN));
        target.writeln("path       : " + Settings.PATH);
    }
}
