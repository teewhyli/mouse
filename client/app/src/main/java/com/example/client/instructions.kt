package com.example.client

class Instructions(
    private val moveX: Int = -1,
    private val moveY: Int = -1,
    private val actionType: ActionType? = null,
    private val operationKind: CommandType? = null,
    private val inputStr: String? = null) {

    enum class CommandType {
        OP_MOVE, OP_CLICK_DOWN, OP_CLICK_UP, OP_RIGHT_CLICK, OP_TYPING
    }

    enum class ActionType(val value: Int) {
        ACTION_DOWN(0),
        ACTION_UP(1),
        ACTION_MOVE(2),
        ACTION_POINTER_DOWN(5),
        ACTION_POINTER_UP(6);

        companion object {
            private val map = ActionType.values().associateBy(ActionType::value)
            fun fromInt(type: Int) = map[type]
        }
    }

    override fun toString(): String {
        val commands: String = when (operationKind) {
            CommandType.OP_CLICK_DOWN -> "click down"
            CommandType.OP_CLICK_UP -> "click up"
            CommandType.OP_MOVE -> "move"
            CommandType.OP_RIGHT_CLICK -> "right click"
            CommandType.OP_TYPING -> "typing"
            else -> "wrong operation"
        }
        return "OperationData [operationKind=$commands, actionType=$actionType, moveX=$moveX, moveY=$moveY, inputStr=$inputStr]";
    }
}