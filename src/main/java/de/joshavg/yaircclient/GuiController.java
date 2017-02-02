package de.joshavg.yaircclient;

import com.eclipsesource.json.JsonObject;
import de.joshavg.yaircclient.api.Client;
import de.joshavg.yaircclient.api.listener.ApiListener;
import de.joshavg.yaircclient.gui.GuiListener;
import de.joshavg.yaircclient.gui.MainForm;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;
import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class GuiController {

    private static final Logger LOG = LoggerFactory.getLogger(GuiController.class);

    private final MainForm form;
    private final Client client;

    @Inject
    GuiController(MainForm form, Client client) {
        this.form = form;
        this.client = client;
    }

    void start(Brabbel brabbel) {
        Method[] methods = brabbel.getClass().getMethods();

        addGuiListeners(brabbel, methods);
        addApiListeners(brabbel, methods);

        JsonObject cfg = brabbel.settings().read();
        boolean autojoin = cfg.getBoolean("autojoin", false);
        if (autojoin) {
            client.addListener(brabbel.autoJoin());
        }

        form.setVisible(true);

        boolean autoconnect = cfg.getBoolean("autoconnect", false);
        if (autoconnect) {
            brabbel.connect().connect();
        }
    }

    private void addApiListeners(Brabbel brabbel, Method[] methods) {
        Arrays.stream(methods)
            .filter(m -> m.getReturnType() == ApiListener.class)
            .map(m -> {
                try {
                    return (ApiListener) m.invoke(brabbel);
                } catch (ReflectiveOperationException e) {
                    LOG.error("error instantiating listener", e);
                }
                return null;
            })
            .filter(Objects::nonNull)
            .forEach(client::addListener);
    }

    private void addGuiListeners(Brabbel brabbel, Method[] methods) {
        Arrays.stream(methods)
            .filter(m -> m.getReturnType() == GuiListener.class)
            .map(m -> {
                try {
                    return (GuiListener) m.invoke(brabbel);
                } catch (ReflectiveOperationException e) {
                    LOG.error("error instantiating listener", e);
                }
                return null;
            })
            .filter(Objects::nonNull)
            .forEach(form::addListener);
    }
}
