package de.joshavg.yaircclient.gui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class MainForm extends JFrame {

    private static final Logger LOG = LoggerFactory.getLogger(MainForm.class);

    private final JLabel indicator;

    private final JLabel readIndicator;

    private final JTextField mainInput;

    private final List<GuiListener> listeners;

    private JScrollPane sspMainOutput;

    private OutputTarget currentTarget;

    private TrayIcon trayIcon;

    public MainForm() {
        mainInput = new JTextField();
        indicator = new JLabel("brabbel");
        listeners = new ArrayList<>();
        currentTarget = OutputFactory.createSystem();

        readIndicator = new JLabel("!");
        readIndicator.setForeground(Color.RED);
        readIndicator.setVisible(false);

        buildTray();

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("brabbel");
        setSize(700, 600);

        buildGui();

        final MainForm gui = this;
        mainInput.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    listeners.forEach(l -> l.messageTyped(mainInput.getText(), gui));
                    mainInput.setText("");
                }
            }
        });
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
                trayIcon = new TrayIcon(img, "brabbel");
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
            e.printStackTrace();
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
