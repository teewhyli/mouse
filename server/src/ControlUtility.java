import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.Locale;

public class ControlUtility {

    private final Robot r = new Robot();

    public ControlUtility() throws AWTException { }

    //TODO - get rid of all the ugly switch cases with something better...

    public void processInstructions(Instructions instructions){
        switch (instructions.getOperationKind()){
            case OP_TYPING:
                processKeyboardEvent(instructions);
                break;
            case OP_MOVE:
            case OP_LEFT_CLICK:
                try {
                    processMouseEvent(instructions);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    private void processMouseEvent(Instructions instructions) throws Exception {
        switch (instructions.getActionType()){
            case ACTION_MOVE:
                int cur_x = MouseInfo.getPointerInfo().getLocation().x;
                int cur_y = MouseInfo.getPointerInfo().getLocation().y;
                r.mouseMove(cur_x + instructions.getMoveX(), cur_y + instructions.getMoveY());
                break;
            case ACTION_UP:
                r.mousePress(MouseEvent.BUTTON1_DOWN_MASK);
                r.mouseRelease(MouseEvent.BUTTON1_DOWN_MASK);
                break;
            default:
                throw new Exception("Incorrect Action Type"); //TODO - make except more specific?
        }
    }

    private void processKeyboardEvent(Instructions instructions){
        String letter = instructions.getInput().toLowerCase(Locale.ROOT);

        System.out.println(letter);

        if (!KeyEventMapping.containsKey(letter)){
            return;
        }

        switch (instructions.getActionType()){
            case ACTION_UP:
                r.keyRelease(KeyEventMapping.getKeyEvent(letter));
                break;
            case ACTION_DOWN:
                r.keyPress(KeyEventMapping.getKeyEvent(letter));
                break;
        }
    }

    private static void mouseMove(){}
    private static void leftClick(){}
    private static void rightClick(){}
    private static void typeSingleCharacter(char c){}
    private static void backspace(){}
}
