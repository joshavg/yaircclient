package de.joshavg.yaircclient.gui;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.Image;
import java.awt.KeyboardFocusManager;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class MainForm extends JFrame {

    private static final String APPLICATION_TITLE = "brabbel";

    private static final Logger LOG = LoggerFactory.getLogger(MainForm.class);

    private final JLabel indicator;

    private final JLabel readIndicator;

    private final JTextField mainInput;

    private final transient List<GuiListener> listeners;

    private JScrollPane sspMainOutput;

    private OutputTarget currentTarget;

    private transient TrayIcon trayIcon;

    @Inject
    public MainForm(OutputFactory outputFactory) {
        mainInput = new JTextField();
        indicator = new JLabel(APPLICATION_TITLE);
        listeners = new ArrayList<>();
        currentTarget = outputFactory.getSystem();

        readIndicator = new JLabel("!");
        readIndicator.setForeground(Color.RED);
        readIndicator.setVisible(false);

        buildTray();

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle(APPLICATION_TITLE);
        setSize(700, 600);

        buildGui();

        mainInput.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                handleMainInputKeyEvent(e);
            }
        });
        addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                mainInput.requestFocusInWindow();
            }
        });
    }

    private void handleMainInputKeyEvent(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_ENTER:
                listeners.forEach(l -> l.messageTyped(mainInput.getText(), this));
                mainInput.setText("");
                break;
            case KeyEvent.VK_UP:
                listeners.forEach(l -> l.arrowUpPressed(this, mainInput));
                break;
            case KeyEvent.VK_DOWN:
                listeners.forEach(l -> l.arrowDownPressed(this, mainInput));
                break;
            case KeyEvent.VK_TAB:
                e.consume();
                listeners.forEach(l -> l.tabPressed(this, mainInput));
                break;
            default:
                // do nothing, normal input
        }
    }

    private Image loadIcon() {
        ClassLoader loader = MainForm.class.getClassLoader();
        URL iconResource = loader.getResource("tray.png");

        if (iconResource == null) {
            LOG.warn("icon image could not be found");
            return null;
        }

        try {
            return ImageIO.read(iconResource);
        } catch (IOException e) {
            LOG.warn("error loading icon image", e);
        }

        return null;
    }

    private void buildTray() {
        if (!SystemTray.isSupported()) {
            return;
        }

        Image img = loadIcon();

        if (img != null) {
            try {
                trayIcon = new TrayIcon(img, APPLICATION_TITLE);
                trayIcon.setImageAutoSize(true);
                SystemTray.getSystemTray().add(trayIcon);
            } catch (AWTException e) {
                LOG.warn("error adding icon to system tray", e);
            }
        }
    }

    public void addListener(GuiListener l) {
        listeners.add(l);
    }

    private void buildGui() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
            | UnsupportedLookAndFeelException e) {
            LOG.warn("could not set look and feel", e);
        }

        Image img = loadIcon();
        if (img != null) {
            setIconImage(img);
        }

        Container contentPane = getContentPane();
        sspMainOutput = new JScrollPane(currentTarget);

        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.add(indicator, BorderLayout.WEST);
        inputPanel.add(readIndicator, BorderLayout.EAST);
        inputPanel.add(mainInput, BorderLayout.CENTER);
        mainInput.setFont(Font.decode("Monospaced"));
        mainInput.setForeground(Color.LIGHT_GRAY);
        mainInput.setCaretColor(Color.LIGHT_GRAY);
        mainInput.setBackground(Color.BLACK);
        mainInput.setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS,
            Collections.emptySet());

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, inputPanel, sspMainOutput);

        contentPane.add(splitPane);
    }

    public void setActiveTarget(OutputTarget target) {
        OutputTarget oldTarget = currentTarget;
        this.currentTarget = target;
        sspMainOutput.setViewportView(target);
        indicator.setText(target.getTarget());

        listeners.forEach(l -> l.targetChanged(oldTarget, this));
    }

    public OutputTarget getCurrentTarget() {
        return currentTarget;
    }

    public void showNotification(String caption, String text) {
        if (trayIcon == null) {
            return;
        }

        trayIcon.displayMessage(caption, text, TrayIcon.MessageType.INFO);
    }

    public void showReadIndicator(boolean show) {
        readIndicator.setVisible(show);
    }

}
