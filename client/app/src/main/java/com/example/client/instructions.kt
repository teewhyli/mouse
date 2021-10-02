package com.example.client

data class Instructions(
    private val moveX: Int = -1,
    private val moveY: Int = -1,
    private val actionType: ActionType? = null,
    private val instructionType: InstructionType? = null,
    private val input: String? = null) {

    enum class InstructionType {
        OP_MOVE, OP_LEFT_CLICK, OP_RIGHT_CLICK, OP_TYPING
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
        val commands: String = when (instructionType) {
            InstructionType.OP_LEFT_CLICK -> "left click"
            InstructionType.OP_MOVE -> "move"
            InstructionType.OP_RIGHT_CLICK -> "right click"
            InstructionType.OP_TYPING -> "typing"
            else -> "wrong operation"
        }
        return "InstructionData [instructionType=$commands, actionType=$actionType, moveX=$moveX, moveY=$moveY, input=$input]"
    }
}