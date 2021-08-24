import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

public class KeyEventMapping {
    private static final Map<String, Integer> mapping = new HashMap<>();

    static {
        for (int i = 'a', j = KeyEvent.VK_A; i<='z'; i++,j++) {
            mapping.put(Character.toString(i), j);
        }

        for (int i = '0', j = KeyEvent.VK_0; i<='9'; i++, j++){
            mapping.put(Character.toString(i), j);
        }

        mapping.put("space", KeyEvent.VK_SPACE);
        mapping.put("shift", KeyEvent.VK_SHIFT);
        mapping.put("bspace", KeyEvent.VK_BACK_SPACE);
        mapping.put("enter", KeyEvent.VK_ENTER);
        mapping.put("equals", KeyEvent.VK_EQUALS);
        mapping.put("quote", KeyEvent.VK_QUOTE);
        mapping.put("`", KeyEvent.VK_BACK_QUOTE);
        mapping.put("-", KeyEvent.VK_MINUS);
        mapping.put("/", KeyEvent.VK_SLASH);
        mapping.put("\\", KeyEvent.VK_BACK_SLASH);
        mapping.put("[", KeyEvent.VK_OPEN_BRACKET);
        mapping.put("]", KeyEvent.VK_CLOSE_BRACKET);
        mapping.put(",", KeyEvent.VK_COMMA);
        mapping.put(".", KeyEvent.VK_PERIOD);
    }

    public static int getKeyEvent(String input){
        return mapping.get(input);
    }

    public static boolean containsKey(String input){
        return mapping.containsKey(input);
    }
}
