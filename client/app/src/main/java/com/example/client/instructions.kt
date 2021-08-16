package com.example.client

class Instructions {

    enum class CommandType {
        OP_MOVE, OP_CLICK_DOWN, OP_CLICK_UP, OP_RIGHT_CLICK, OP_DEL_TEXT, OP_TYPING
    }

    // only valid when operationKind is OPERATION_MOVE
    var moveX = -1
    var moveY = -1
    var operationKind: CommandType? = null
    var inputStr: String? = null

    override fun toString(): String {
        val commands: String = when (operationKind) {
            CommandType.OP_CLICK_DOWN -> "click down"
            CommandType.OP_CLICK_UP -> "click up"
            CommandType.OP_MOVE -> "move"
            CommandType.OP_RIGHT_CLICK -> "right click"
            CommandType.OP_TYPING -> "typing"
            else -> "wrong operation"
        }
        return "OperationData [operationKind=$commands, moveX=$moveX, moveY=$moveY, inputStr=$inputStr]"
    }
}