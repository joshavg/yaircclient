package de.joshavg.yaircclient.gui;

import de.joshavg.yaircclient.api.listener.ApiListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;


public class MainForm extends JFrame implements ApiListener {

    private final JLabel indicator;

    private final JTextField mainInput;

    private final List<GuiListener> listeners;

    private JScrollPane sspMainOutput;

    private OutputTarget currentTarget;

    public MainForm() {
        mainInput = new JTextField();
        indicator = new JLabel("brabbel");
        listeners = new ArrayList<>();
        currentTarget = OutputFactory.createSystem();

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
    }

    public OutputTarget getCurrentTarget() {
        return currentTarget;
    }

}
