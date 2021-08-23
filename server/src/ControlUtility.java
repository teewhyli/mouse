import java.awt.*;

public class ControlUtility {

    private final Robot r = new Robot();

    public ControlUtility() throws AWTException { }

    public void processInstructions(Instructions instructions){
        switch(instructions.getOperationKind()){
            case OP_TYPING:
                processKeyboardEvent(instructions);
            case OP_MOVE:
        }
    }

    private void processMouseEvent(Instructions instructions){}
    private void processKeyboardEvent(Instructions instructions){
        Character letter = instructions.getInput().toCharArray()[0];
        switch (instructions.getActionType()){
            case ACTION_UP:
                if(KeyEventMapping.mapping.containsKey(letter)) {
                    System.out.println((KeyEventMapping.mapping.get(letter)));
                }
                break;
            case ACTION_DOWN:
//                if(KeyEventMapping.mapping.containsKey(letter)) {
//                    System.out.println((KeyEventMapping.mapping.get(letter)));
//                }
                break;
            case ACTION_MOVE:
                break;
            case ACTION_POINTER_DOWN:
                break;
            case ACTION_POINTER_UP:
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + instructions.getActionType());
        }
    }

    private static void mouseMove(){}
    private static void leftClick(){}
    private static void rightClick(){}
    private static void typeSingleCharacter(char c){}
    private static void backspace(){}
}
