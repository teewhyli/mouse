public class OperationData {

    public enum CommandType {
        OP_MOVE,
        OP_CLICK_DOWN,
        OP_CLICK_UP,
        OP_RIGHT_CLICK,
        OP_DEL_TEXT,
        OP_TYPING
    }


    private CommandType operationKind;

    // only valid when operationKind is OPERATION_MOVE
    private int moveX = -1;
    private int moveY = -1;

    //
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
        String oper;
        switch (operationKind) {
            case OP_CLICK_DOWN:
                oper = "click down";
                break;
            case OP_CLICK_UP:
                oper = "click up";
                break;
            case OP_MOVE:
                oper = "move";
                break;
            case OP_RIGHT_CLICK:
                oper = "right click";
                break;
            case OP_TYPING:
                oper = "typing";
                break;
            default:
                oper = "wrong operation";
                break;
        }
        return "OperationData [operationKind=" + oper + ", moveX=" + moveX + ", moveY=" + moveY + ", inputStr=" + inputStr + "]";
    }
}