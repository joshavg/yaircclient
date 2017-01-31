package de.joshavg.yaircclient.api;

import dagger.Module;
import dagger.Provides;
import de.joshavg.yaircclient.api.listener.Disconnector;
import de.joshavg.yaircclient.api.listener.Renamer;
import de.joshavg.yaircclient.api.listener.UsersCollector;

import javax.inject.Inject;
import javax.inject.Singleton;

@Module
public class ClientFactory {

    private UsersCollector collector;

    @Provides
    @Singleton
    @Inject
    Client provideClient(Renamer renamer, Disconnector disconnector) {
        Client client = new Client();
        collector = new UsersCollector(client);

        client.addListener(renamer);
        client.addListener(disconnector);
        client.addListener(collector);
        return client;
    }

    @Provides
    @Singleton
    @Inject
    UsersCollector provideUsersCollector(@SuppressWarnings("unused") Client client) {
        // workaround dependency to the client so that this method will always be called after provideClient
        return collector;
    }

}
