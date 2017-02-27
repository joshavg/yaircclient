package de.joshavg.yaircclient.gui;

import static de.joshavg.yaircclient.gui.ActionType.CHANNEL;
import static de.joshavg.yaircclient.gui.ActionType.HIGHLIGHT;
import static de.joshavg.yaircclient.gui.ActionType.MESSAGE;
import static de.joshavg.yaircclient.gui.ActionType.NOTICE;

import java.awt.Color;
import java.awt.Font;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OutputTarget extends JTextPane {

    private static final Logger LOG = LoggerFactory.getLogger(OutputTarget.class);

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
            LOG.error("could not scroll to the bottom", e);
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
