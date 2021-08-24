public class Instructions{

    private int moveX = -1;
    private int moveY = -1;
    private InstructionType instructionType;
    private ActionType actionType;
    private String input;

    public enum InstructionType {
        OP_MOVE,
        OP_LEFT_CLICK,
        OP_RIGHT_CLICK,
        OP_TYPING
        }

    public enum ActionType {
        ACTION_UP, ACTION_DOWN, ACTION_MOVE, ACTION_POINTER_DOWN, ACTION_POINTER_UP
    }

    public InstructionType getOperationKind() {
        return instructionType;
    }

    public void setOperationKind(InstructionType operationKind) {
        this.instructionType = operationKind;
    }

    public void setActionType(ActionType actionType) { this.actionType = actionType; }
    public ActionType getActionType() { return this.actionType; }

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

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    @Override
    public String toString() {
        String commands;
        switch (instructionType) {
            case OP_LEFT_CLICK:
                commands = "left click";
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
        return "OperationData [operationKind=" + commands + ", actionType=" + actionType + ", moveX=" + moveX + ", moveY=" + moveY + ", input=" + input + "]";
    }
}