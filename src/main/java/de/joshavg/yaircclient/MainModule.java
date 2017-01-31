package de.joshavg.yaircclient;

import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;

@Module
class MainModule {

    @Provides
    @Singleton
    static Settings provideSettings() {
        return new Settings();
    }

}
