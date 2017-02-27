package de.joshavg.yaircclient;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
import com.eclipsesource.json.WriterConfig;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Settings {

    public static final String PATH;

    private static final Logger LOG = LoggerFactory.getLogger(Settings.class);

    static {
        String userHome = System.getProperty("user.home");
        PATH = userHome + File.separator + ".config" + File.separator + "brabbel.json";
    }

    private JsonObject createDefaultCfg() {
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

    boolean createIfNotExists() {
        if (new File(PATH).exists()) {
            return false;
        }

        write(createDefaultCfg());

        return true;
    }

    public JsonObject read() {
        try {
            FileReader fileReader = new FileReader(PATH);
            return Json.parse(fileReader).asObject();
        } catch (IOException e) {
            LOG.error(
                "could not read settings file. this is either, because you started the application the first"
                    +
                    " time or your file system is not properly configured", e);
        }

        return createDefaultCfg();
    }

    public synchronized void write(JsonValue value) {
        try (FileWriter writer = new FileWriter(PATH)) {
            value.writeTo(writer, WriterConfig.PRETTY_PRINT);
        } catch (IOException e) {
            LOG.error("could write read settings file", e);
        }
    }

}
