package de.joshavg.yaircclient.api;

import de.joshavg.yaircclient.api.listener.Listeners;

public class ClientFactory {

    private ClientFactory() {
    }

    public static Client getClient() {
        Client client = new Client();
        Listeners.addStandardListeners(client);
        return client;
    }

}
