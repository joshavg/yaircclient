package de.joshavg.yaircclient.gui;

public interface GuiListener {

    default void messageTyped(String message, MainForm gui) {
    }

}
