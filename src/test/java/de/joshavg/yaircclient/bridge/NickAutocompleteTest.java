package de.joshavg.yaircclient.bridge;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.joshavg.yaircclient.api.ResponseParser.Key;
import de.joshavg.yaircclient.api.ResponseParser.ResponseValue;
import de.joshavg.yaircclient.api.listener.UsersCollector;
import de.joshavg.yaircclient.gui.MainForm;
import de.joshavg.yaircclient.gui.OutputTarget;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JTextField;
import org.junit.Before;
import org.junit.Test;

public class NickAutocompleteTest {

    private UsersCollector collector;
    private MainForm gui;
    private OutputTarget target;
    private JTextField field;
    private NickAutocomplete nickAutocomplete;

    @Before
    public void setUp() throws Exception {
        collector = mock(UsersCollector.class);
        gui = mock(MainForm.class);
        target = mock(OutputTarget.class);
        field = mock(JTextField.class);
        nickAutocomplete = new NickAutocomplete(collector);

        when(target.getTarget()).thenReturn("#channel");
        when(gui.getCurrentTarget()).thenReturn(target);
    }

    private static Map<Key, ResponseValue> get366Line() {
        Map<Key, ResponseValue> line366 = new HashMap<>();
        line366.put(Key.CMD, new ResponseValue("366"));
        return line366;
    }

    private static Map<Key, ResponseValue> get353Line() {
        Map<Key, ResponseValue> line353 = new HashMap<>();
        line353.put(Key.CMD, new ResponseValue("353"));
        line353.put(Key.PAYLOAD, new ResponseValue("aa bb"));
        return line353;
    }

    @Test
    public void inputTextNotChangedWhenInputIsEmpty() {
        nickAutocomplete.tabPressed(gui, field);

        Map<Key, ResponseValue> line353 = get353Line();
        collector.parsed(line353, null);

        Map<Key, ResponseValue> line366 = get366Line();
        collector.parsed(line366, null);

        verify(field, times(0)).setText(any());
    }

    @Test
    public void inputTextNotChangedWhenNoMatchIsFound() {
        when(field.getText()).thenReturn("c");

        nickAutocomplete.tabPressed(gui, field);

        Map<Key, ResponseValue> line353 = get353Line();
        collector.parsed(line353, null);

        Map<Key, ResponseValue> line366 = get366Line();
        collector.parsed(line366, null);

        verify(field, times(0)).setText(any());
    }

    @Test
    public void inputTextBecomesNickName() {
        when(field.getText()).thenReturn("a");

        nickAutocomplete.tabPressed(gui, field);

        Map<Key, ResponseValue> line353 = get353Line();
        collector.parsed(line353, null);

        Map<Key, ResponseValue> line366 = get366Line();
        collector.parsed(line366, null);

        verify(field, times(0)).setText(eq("aa"));
    }

    @Test
    public void nickNameGetsAppendedToMessage() {
        when(field.getText()).thenReturn("chat message to a");

        nickAutocomplete.tabPressed(gui, field);

        Map<Key, ResponseValue> line353 = get353Line();
        collector.parsed(line353, null);

        Map<Key, ResponseValue> line366 = get366Line();
        collector.parsed(line366, null);

        verify(field, times(0)).setText(eq("chat message to aa"));
    }

}
