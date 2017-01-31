package de.joshavg.yaircclient;

import dagger.Component;
import de.joshavg.yaircclient.api.Client;
import de.joshavg.yaircclient.api.ClientFactory;
import de.joshavg.yaircclient.api.listener.ApiListenerModule;
import de.joshavg.yaircclient.api.listener.UsersCollector;
import de.joshavg.yaircclient.bridge.*;
import de.joshavg.yaircclient.gui.GuiModule;
import de.joshavg.yaircclient.gui.listener.Connect;
import de.joshavg.yaircclient.gui.listener.Exit;
import de.joshavg.yaircclient.gui.listener.JoinLeave;
import de.joshavg.yaircclient.gui.listener.MessageHistory;
import de.joshavg.yaircclient.gui.listener.MessageSend;
import de.joshavg.yaircclient.gui.listener.NickChange;
import de.joshavg.yaircclient.gui.listener.SettingsListener;
import de.joshavg.yaircclient.gui.listener.Windows;
import de.joshavg.yaircclient.gui.listener.GuiListenerModule;
import javax.inject.Singleton;

@Component(modules = {
    MainModule.class,
    GuiModule.class,
    GuiListenerModule.class,
    ClientFactory.class,
    ApiListenerModule.class
})
@Singleton
interface Brabbel {
    Settings settings();
    GuiController guiController();
    Client client();

    Connect connect();
    Exit exit();
    JoinLeave joinLeave();
    MessageHistory messageHistory();
    MessageSend messageSend();
    NickChange nickChange();
    SettingsListener settingsListener();
    Windows windows();

    ApiLogger apiLogger();
    AutoJoin autoJoin();
    JoinDisplay joinDisplay();
    MessageDisplay messageDisplay();
    MessageReadStatus messageReadStatus();
    PartDisplay partDisplay();
    UsersDisplay usersDisplay();
    NickAutocomplete nickAutocomplete();
}
