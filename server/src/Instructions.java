import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Instructions {

    private final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public String processCommand(StringBuilder inputLine) {
        String input = inputLine.toString();
        Instructions result = GSON.fromJson(input, Instructions.class);
        if (result.getInputStr() == null || result.getInputStr().equals("")) {
            return null;
        }
        switch(result.getOperationKind()){
            case OP_TYPING:
                return result.getInputStr(); // command
            default:
                return null;
        }
    }

    public enum CommandType {
        OP_MOVE,
        OP_CLICK_DOWN,
        OP_CLICK_UP,
        OP_RIGHT_CLICK,
        OP_DEL_TEXT,
        OP_TYPING
        }

    // only valid when operationKind is OPERATION_MOVE
    private int moveX = -1;
    private int moveY = -1;
    private CommandType operationKind;
    private String inputStr;

    public CommandType getOperationKind() {
        return operationKind;
    }

    public void setOperationKind(CommandType operationKind) {
        this.operationKind = operationKind;
    }

    public int getMoveX() {
        return moveX;
    }

    public void setMoveX(int moveX) {
        this.moveX = moveX;
    }

    public int getMoveY() {
        return moveY;
    }

    public void setMoveY(int moveY) {
        this.moveY = moveY;
    }

    public String getInputStr() {
        return inputStr;
    }

    public void setInputStr(String inputStr) {
        this.inputStr = inputStr;
    }

    @Override
    public String toString() {
        String commands;
        switch (operationKind) {
            case OP_CLICK_DOWN:
                commands = "click down";
                break;
            case OP_CLICK_UP:
                commands = "click up";
                break;
            case OP_MOVE:
                commands = "move";
                break;
            case OP_RIGHT_CLICK:
                commands = "right click";
                break;
            case OP_TYPING:
                commands = "typing";
                break;
            default:
                commands = "wrong operation";
                break;
        }
        return "OperationData [operationKind=" + commands + ", moveX=" + moveX + ", moveY=" + moveY + ", inputStr=" + inputStr + "]";
    }
}