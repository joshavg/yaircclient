package de.joshavg.yaircclient;

import com.eclipsesource.json.*;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Settings {

    public static final String PATH;

    static {
        String userHome = System.getProperty("user.home");
        PATH = userHome + File.separator + ".config" + File.separator + "brabbel.json";
    }

    private static JsonObject createDefaultCfg() {
        JsonObject config = Json.parse("{}").asObject();
        JsonObject cx = Json.parse("{}").asObject();
        cx.set("url", "");
        cx.set("port", 6667);
        JsonArray channels = Json.parse("[]").asArray();

        config.set("connection", cx);
        config.set("channels", channels);

        config.set("nick", "");
        config.set("autoconnect", false);
        config.set("autojoin", false);

        return config;
    }

    static boolean createIfNotExists() {
        if(new File(PATH).exists()) {
            return false;
        }

        write(createDefaultCfg());

        return true;
    }

    public static JsonObject read() {
        try {
            FileReader fileReader = new FileReader(PATH);
            return Json.parse(fileReader).asObject();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return createDefaultCfg();
    }

    public static synchronized void write(JsonValue value) {
        try (FileWriter writer = new FileWriter(PATH)) {
            value.writeTo(writer, WriterConfig.PRETTY_PRINT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}