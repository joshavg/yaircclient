package de.joshavg.yaircclient.gui;

import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;

@Module
public class GuiModule {

    private GuiModule() {
        // di class
    }

    @Provides
    @Singleton
    static OutputFactory provideOutputFactory() {
        return new OutputFactory();
    }

}
