package de.joshavg.yaircclient.gui.listener;

import de.joshavg.yaircclient.RingBuffer;
import de.joshavg.yaircclient.gui.GuiListener;
import de.joshavg.yaircclient.gui.MainForm;
import javax.swing.JTextField;

public class MessageHistory implements GuiListener {

    private final RingBuffer<String> buffer;

    MessageHistory() {
        buffer = new RingBuffer<>();
    }

    @Override
    public void arrowUpPressed(MainForm gui, JTextField field) {
        field.setText(buffer.prev());
    }

    @Override
    public void arrowDownPressed(MainForm gui, JTextField field) {
        field.setText(buffer.next());
    }

    @Override
    public void messageTyped(String message, MainForm gui) {
        buffer.add(message);
    }
}
