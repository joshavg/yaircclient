package de.joshavg.yaircclient.gui;

import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;

@Module
public class GuiModule {

    @Provides
    @Singleton
    static OutputFactory provideOutputFactory() {
        return new OutputFactory();
    }

}
