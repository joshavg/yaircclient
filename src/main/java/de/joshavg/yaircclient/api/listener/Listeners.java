package de.joshavg.yaircclient.api.listener;

import de.joshavg.yaircclient.api.Client;

public class Listeners {

    private Listeners() {
    }

    public static void addStandardListeners(Client client) {
        client.addListener(new Renamer());
        client.addListener(new Channels());
        client.addListener(new Disconnector());
    }

}
