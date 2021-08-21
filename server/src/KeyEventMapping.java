import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

public class KeyEventMapping {
    public static final Map<Character, Integer> mapping = new HashMap<>();

    static {
        for (int i = 'a', j = KeyEvent.VK_A; i<='z'; i++,j++) {
            mapping.put((char)i, j);
        }

        System.out.println(mapping);
    }
}
