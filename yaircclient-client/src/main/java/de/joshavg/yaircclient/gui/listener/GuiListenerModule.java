package de.joshavg.yaircclient.gui.listener;

import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;

@Module
public class GuiListenerModule {

    @Provides
    @Singleton
    MessageHistory provideMessageHistory() {
        return new MessageHistory();
    }

}
