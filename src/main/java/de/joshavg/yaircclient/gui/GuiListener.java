package de.joshavg.yaircclient.gui;

import javax.swing.*;

public interface GuiListener {

    default void messageTyped(String message, MainForm gui) {
    }

    default void targetChanged(OutputTarget oldTarget, MainForm gui) {
    }

    default void arrowUp(MainForm gui, JTextField field) {
    }

    default void arrowDown(MainForm gui, JTextField field) {
    }

}
