package de.joshavg.yaircclient.api.listener;

import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;

@Module
public class ApiListenerModule {

    @Provides
    @Singleton
    Renamer provideRenamer() {
        return new Renamer();
    }

    @Provides
    @Singleton
    Disconnector provideDisconnector() {
        return new Disconnector();
    }

}
