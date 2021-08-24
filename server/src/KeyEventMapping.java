import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

public class KeyEventMapping {
    public static final Map<Character, Integer> mapping = new HashMap<>();

    static {
        for (int i = 'a', j = KeyEvent.VK_A; i<='z'; i++,j++) {
            mapping.put((char)i, j);
        }

        mapping.put('0', KeyEvent.VK_SPACE);
        mapping.put('1', KeyEvent.VK_SHIFT);
        mapping.put('2', KeyEvent.VK_DELETE);
        mapping.put('3', KeyEvent.VK_ENTER);
        mapping.put('4', KeyEvent.VK_EQUALS);
        mapping.put('5', KeyEvent.VK_QUOTE);
        mapping.put('`', KeyEvent.VK_BACK_QUOTE);
        mapping.put('-', KeyEvent.VK_MINUS);
        mapping.put('/', KeyEvent.VK_SLASH);
        mapping.put('\\', KeyEvent.VK_BACK_SLASH);
        mapping.put('[', KeyEvent.VK_OPEN_BRACKET);
        mapping.put(']', KeyEvent.VK_CLOSE_BRACKET);
        mapping.put(',', KeyEvent.VK_COMMA);
        mapping.put('.', KeyEvent.VK_PERIOD);
    }
}
