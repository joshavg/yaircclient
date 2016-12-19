package de.joshavg.yaircclient.gui;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;

import static de.joshavg.yaircclient.gui.ActionType.*;

public class OutputTarget extends JTextPane {

    private final String target;

    OutputTarget(String target) {
        this.target = target;
        createStyles();

        setEditable(false);
        setFont(Font.decode("Monospaced"));
        setForeground(Color.LIGHT_GRAY);
        setCaretColor(Color.LIGHT_GRAY);
        setBackground(Color.BLACK);
    }

    private void createStyles() {
        Style channelAction = addStyle(CHANNEL.name(), null);
        StyleConstants.setForeground(channelAction, Color.ORANGE);

        Style error = addStyle(ActionType.ERROR.name(), null);
        StyleConstants.setForeground(error, Color.RED);

        Style highlight = addStyle(HIGHLIGHT.name(), null);
        StyleConstants.setForeground(highlight, Color.MAGENTA);
        StyleConstants.setItalic(highlight, true);

        addStyle(MESSAGE.name(), null);

        Style notice = addStyle(NOTICE.name(), null);
        StyleConstants.setForeground(notice, Color.CYAN);
    }

    public void writeln(String text, ActionType type) {
        StyledDocument doc = getStyledDocument();
        try {
            String write = text + System.lineSeparator();
            doc.insertString(doc.getLength(), write, getStyle(type.name()));
        } catch (BadLocationException e) {
            e.printStackTrace();
        }

        jumpToEnd();
    }

    public void writeln(String text) {
        writeln(text, MESSAGE);
    }

    public void jumpToEnd() {
        int length = getStyledDocument().getLength();
        setCaretPosition(length - 1);
    }

    public String getTarget() {
        return target;
    }
}
