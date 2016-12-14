package de.joshavg.yaircclient.gui;

import de.joshavg.yaircclient.api.listener.ApiListener;
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


public class MainForm extends JFrame implements ApiListener {

    private static final Logger LOG = LoggerFactory.getLogger(MainForm.class);

    private final JLabel indicator;

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

        buildTray();

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("brabbel");
        setSize(800, 400);

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

    private void buildTray() {
        if (!SystemTray.isSupported()) {
            return;
        }

        ClassLoader loader = MainForm.class.getClassLoader();
        URL iconResource = loader.getResource("tray.png");

        if (iconResource == null) {
            LOG.warn("tray icon image could not be found");
            return;
        }

        try {
            trayIcon = new TrayIcon(ImageIO.read(iconResource), "brabbel");
            SystemTray.getSystemTray().add(trayIcon);
        } catch (IOException e) {
            LOG.warn("error loading tray icon image", e);
        } catch (AWTException e) {
            LOG.warn("error adding icon to system tray", e);
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

        Container contentPane = getContentPane();
        sspMainOutput = new JScrollPane(currentTarget);

        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.add(indicator, BorderLayout.WEST);
        inputPanel.add(mainInput, BorderLayout.CENTER);
        mainInput.setFont(Font.decode("Monospaced"));
        mainInput.setForeground(Color.LIGHT_GRAY);
        mainInput.setCaretColor(Color.LIGHT_GRAY);
        mainInput.setBackground(Color.BLACK);

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, inputPanel, sspMainOutput);

        contentPane.add(splitPane);
    }

    public void setActiveTarget(OutputTarget target) {
        this.currentTarget = target;
        sspMainOutput.setViewportView(target);
        indicator.setText(target.getTarget());
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

}
