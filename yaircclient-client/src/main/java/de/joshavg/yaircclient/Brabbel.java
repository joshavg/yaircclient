package de.joshavg.yaircclient;

import dagger.Component;
import de.joshavg.yaircclient.api.Client;
import de.joshavg.yaircclient.api.ClientFactory;
import de.joshavg.yaircclient.api.listener.ApiListenerModule;
import de.joshavg.yaircclient.api.listener.Disconnector;
import de.joshavg.yaircclient.api.listener.Renamer;
import de.joshavg.yaircclient.bridge.ApiLogger;
import de.joshavg.yaircclient.bridge.AutoJoin;
import de.joshavg.yaircclient.bridge.JoinDisplay;
import de.joshavg.yaircclient.bridge.MessageDisplay;
import de.joshavg.yaircclient.bridge.MessageReadStatus;
import de.joshavg.yaircclient.bridge.NickAutocomplete;
import de.joshavg.yaircclient.bridge.PartDisplay;
import de.joshavg.yaircclient.bridge.UsersDisplay;
import de.joshavg.yaircclient.gui.GuiModule;
import de.joshavg.yaircclient.gui.listener.Connect;
import de.joshavg.yaircclient.gui.listener.Exit;
import de.joshavg.yaircclient.gui.listener.GuiListenerModule;
import de.joshavg.yaircclient.gui.listener.JoinLeave;
import de.joshavg.yaircclient.gui.listener.MessageHistory;
import de.joshavg.yaircclient.gui.listener.MessageSend;
import de.joshavg.yaircclient.gui.listener.NickChange;
import de.joshavg.yaircclient.gui.listener.SettingsListener;
import de.joshavg.yaircclient.gui.listener.Windows;
import javax.inject.Singleton;

@SuppressWarnings("unused")
@Component(modules = {
    MainModule.class,
    GuiModule.class,
    GuiListenerModule.class,
    ClientFactory.class,
    ApiListenerModule.class
})
@Singleton
interface Brabbel {

    // @formatter:off

    // core dependencies
    Settings settings();
    GuiController guiController();
    Client client();

    // gui listeners
    Connect connect();
    Exit exit();
    JoinLeave joinLeave();
    MessageHistory messageHistory();
    MessageSend messageSend();
    NickChange nickChange();
    SettingsListener settingsListener();
    Windows windows();

    // bridge listeners
    ApiLogger apiLogger();
    AutoJoin autoJoin();
    JoinDisplay joinDisplay();
    MessageDisplay messageDisplay();
    MessageReadStatus messageReadStatus();
    NickAutocomplete nickAutocomplete();
    PartDisplay partDisplay();
    UsersDisplay usersDisplay();

    // api listeners
    Disconnector disconnector();
    Renamer renamer();
    // @formatter:on
}
