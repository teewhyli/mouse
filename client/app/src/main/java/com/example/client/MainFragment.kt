package com.example.client

import android.annotation.SuppressLint
import android.os.Bundle
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
    private lateinit var imm: InputMethodManager
    private lateinit var rootView: View
    private var touchX: Float = 0.0F
    private var touchY: Float = 0.0F
    private var lastLandMark: Long = 0L
    private var isKeyboardOn: AtomicBoolean = AtomicBoolean(false)

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val extFloatingButton: ExtendedFloatingActionButton = view.findViewById(R.id.extended_fab)
        val keyboardEditText: EditText = view.findViewById(R.id.keyboard)
        val layout: View = view.findViewById(R.id.touch_board)

        extFloatingButton.setOnClickListener {
            keyboardEditText.apply {
                requestFocus()
                showSoftKeyboard(this)
            }
        }

        keyboardEditText.apply {
            inputType = 0
            setOnKeyListener { _, keyCode, event ->
                Log.d("KeyboardEditText", "event: $event")

                if (keyCode == KEYCODE_UNKNOWN) {
                    return@setOnKeyListener true
                }

                Instructions(
                    instructionType = Instructions.InstructionType.OP_TYPING,
                    actionType = Instructions.ActionType.fromInt(event.action),
                    input = getCharacterDisplayLabel(event)
                ).also { sendToMessageQueue(it) }

                onKeyUp(keyCode, event)
                true
            }
        }

        layout.apply {
            setOnTouchListener { _, event ->
                Log.i("OnTouch", "$event")

                if (isKeyboardOn.get() && event.action == ACTION_UP) {
                    hideSoftKeyboard(this)
                    return@setOnTouchListener true
                }

                if (event.action == ACTION_DOWN) {
                    return@setOnTouchListener true
                }

                if (event.eventTime - lastLandMark > 50) {
                    event.apply {
                        touchX = x
                        touchY = y
                        lastLandMark = eventTime
                    }
                }

                if (!isKeyboardOn.get()) {

                    if (event.action == ACTION_MOVE && event.eventTime - event.downTime >= 150) {

                        Instructions(
                            moveX = (event.x.toInt() - touchX.toInt()) / 2,
                            moveY = (event.y.toInt() - touchY.toInt()) / 2,
                            instructionType = Instructions.InstructionType.OP_MOVE,
                            actionType = Instructions.ActionType.fromInt(event.action)
                        ).also { sendToMessageQueue(it) }

                    } else if (event.eventTime - event.downTime < 150) {

                        Instructions(
                            instructionType = Instructions.InstructionType.OP_LEFT_CLICK,
                            actionType = Instructions.ActionType.fromInt(event.action)
                        ).also { sendToMessageQueue(it) }
                    }
                }
                return@setOnTouchListener true
            }
        }
    }

    private fun sendToMessageQueue(instructions: Instructions) {
        Message.obtain(TCP.handler).run {
            obj = processCommand(instructions)
            sendToTarget()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        TCP.disconnect()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_main, container, false).also {
            rootView = it
        }
    }

    private fun processCommand(instructions: Instructions): String? {
        return Gson().toJson(instructions, instructions::class.java)
    }

    private fun showSoftKeyboard(view: View) {
        isKeyboardOn.apply {
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
            set(true)
        }
    }

    private fun hideSoftKeyboard(view: View) {
        isKeyboardOn.apply {
            imm.hideSoftInputFromWindow(view.applicationWindowToken, 0)
            set(false)
        }
    }

    private fun getCharacterDisplayLabel(event: KeyEvent): String {
        return when(event.keyCode) {
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
        hideSoftKeyboard(rootView)
    }

    companion object {
        /**
         * @param InputMethodManager inputMethodManager.
         * @return A new instance of fragment MainFragment.
         */
        @JvmStatic fun newInstance(inputMethodManager: InputMethodManager) =
            MainFragment().apply { imm = inputMethodManager }
    }
}