package de.joshavg.yaircclient.gui;

import javax.swing.*;

public interface GuiListener {

    default void messageTyped(String message, MainForm gui) {
    }

    default void targetChanged(OutputTarget oldTarget, MainForm gui) {
    }

    default void arrowUpPressed(MainForm gui, JTextField field) {
    }

    default void arrowDownPressed(MainForm gui, JTextField field) {
    }

    default void tabPressed(MainForm gui, JTextField field) {
    }

}
