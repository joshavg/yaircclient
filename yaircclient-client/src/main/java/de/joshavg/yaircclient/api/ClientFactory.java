package de.joshavg.yaircclient.api;

import dagger.Module;
import dagger.Provides;
import de.joshavg.yaircclient.api.listener.UsersCollector;
import javax.inject.Inject;
import javax.inject.Singleton;

@Module
public class ClientFactory {

    @Provides
    @Singleton
    Client provideClient() {
        return new Client();
    }

    @Provides
    @Singleton
    @Inject
    UsersCollector provideUsersCollector(Client client) {
        return new UsersCollector(client);
    }

}
