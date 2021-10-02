package com.example.client

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.KeyEvent
import android.view.KeyEvent.*
import android.view.LayoutInflater
import android.view.MotionEvent.ACTION_MOVE
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.core.content.*
import androidx.fragment.app.Fragment
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.gson.Gson
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean

class MainFragment : Fragment() {
    private lateinit var layout: View
    private lateinit var keyboardEditText: EditText
    private lateinit var imm: InputMethodManager
    private lateinit var extFloatingButton: ExtendedFloatingActionButton
    private lateinit var handler: Handler
    private var touchX: Float = 0.0F
    private var touchY: Float = 0.0F
    private var lastLandMark: Long = 0L
    private var isKeyboardOn: AtomicBoolean = AtomicBoolean(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        handler = ConnectionFactory()
            .get(IConnectionFactory.Type.TCP)
            .connect()!!
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        layout = view.findViewById(R.id.touch_board)
        extFloatingButton = view.findViewById(R.id.extended_fab)
        keyboardEditText = view.findViewById(R.id.keyboard)
        keyboardEditText.inputType = 0

        extFloatingButton.setOnClickListener {
            keyboardEditText.requestFocus()
            showSoftKeyboard(keyboardEditText)
        }

        keyboardEditText.setOnKeyListener { _ , keyCode, event ->
            Log.d("KeyboardEditText", "event: $event")

            val msg = Message.obtain(handler)

            if (keyCode == KEYCODE_UNKNOWN){
                return@setOnKeyListener true
            }

            val instructions = Instructions(
                instructionType = Instructions.InstructionType.OP_TYPING,
                actionType = Instructions.ActionType.fromInt(event.action),
                input = getCharacterDisplayLabel(event)
            )
            msg.obj = processCommand(instructions)
            msg.sendToTarget()
            keyboardEditText.onKeyUp(keyCode, event)

            true
        }

        layout.setOnTouchListener { _, event ->

            Log.i("OnTouch", "$event")
            val msg = Message.obtain(handler)

            if (isKeyboardOn.get() && event.action == ACTION_UP){
                hideSoftKeyboard()
                return@setOnTouchListener true
            }

            if (event.action == ACTION_DOWN){
                return@setOnTouchListener true
            }

            if (event.eventTime - lastLandMark > 50){
                touchX = event.x
                touchY = event.y
                lastLandMark = event.eventTime
            }

            if (!isKeyboardOn.get()){

                if (event.action == ACTION_MOVE && event.eventTime - event.downTime >= 150) {

                    val instructions = Instructions(
                        moveX = (event.x.toInt() - touchX.toInt()) / 2,
                        moveY = (event.y.toInt() - touchY.toInt()) / 2,
                        instructionType = Instructions.InstructionType.OP_MOVE,
                        actionType = Instructions.ActionType.fromInt(event.action))

                    msg.obj = processCommand(instructions)

                    msg.sendToTarget()

                } else if (event.eventTime - event.downTime < 150){

                    val instructions = Instructions(
                        instructionType = Instructions.InstructionType.OP_LEFT_CLICK,
                        actionType = Instructions.ActionType.fromInt(event.action))

                    msg.obj = processCommand(instructions)

                    msg.sendToTarget()

                }
            }
            return@setOnTouchListener true
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        ConnectionFactory().get(IConnectionFactory.Type.TCP).disconnect()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    private fun processCommand(instructions: Instructions): String? {
        val gson = Gson()
        return gson.toJson(instructions, instructions::class.java)
    }

    private fun showSoftKeyboard(editText: EditText) {
        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT)
        isKeyboardOn.set(true)
    }

    private fun hideSoftKeyboard() {
        imm.hideSoftInputFromWindow( extFloatingButton.applicationWindowToken, 0)
        isKeyboardOn.set(false)
    }

    private fun getCharacterDisplayLabel(event: KeyEvent): String {
        return when(event.keyCode){
            KEYCODE_SPACE -> "space"
            KEYCODE_SHIFT_LEFT -> "shift"
            KEYCODE_DEL -> "bspace"
            KEYCODE_ENTER -> "enter"
            KEYCODE_EQUALS -> "equals"
            KEYCODE_APOSTROPHE -> "quote"
            else -> event.keyCharacterMap.getDisplayLabel(event.keyCode).toString()
        }
    }

    override fun onPause() {
        super.onPause()
        hideSoftKeyboard()
    }

    companion object {
        /**
         * @param InputMethodManager inputMethodManager.
         * @return A new instance of fragment MainFragment.
         */
        @JvmStatic fun newInstance(inputMethodManager: InputMethodManager) =
            MainFragment().apply {
                    imm = inputMethodManager
                }
    }
}