package com.example.client

import android.annotation.SuppressLint
import android.os.*
import android.util.Log
import android.view.KeyEvent
import android.view.KeyEvent.*
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.*
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.gson.Gson
import java.io.*
import java.net.Socket
import java.nio.charset.StandardCharsets
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


class MainActivity : AppCompatActivity() {

    private lateinit var layout: View
    private lateinit var extFloatingButton: ExtendedFloatingActionButton
    private lateinit var keyboardEditText: EditText
    private lateinit var imm: InputMethodManager
    private lateinit var sock: Socket
    private lateinit var writeToServer: PrintWriter
    private var mBackgroundHandlerThread: HandlerThread = HandlerThread("Message Handler Thread")
    private val es: ExecutorService = Executors.newCachedThreadPool()
    private lateinit var mBackgroundHandler: Handler

    @SuppressLint("ClickableViewAccessibility") // we're not dealing with swipes.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager

        es.submit{ connect() }

        mBackgroundHandlerThread.start()

        mBackgroundHandler = Handler(mBackgroundHandlerThread.looper){
            val msg: String = it.obj.toString()

            Log.d("handler", msg)

            writeToServer.println(msg)
            writeToServer.flush()
            true
        }

        layout = findViewById(R.id.touch_board)
        extFloatingButton = findViewById(R.id.extended_fab)
        keyboardEditText = findViewById(R.id.keyboard)
        keyboardEditText.inputType = 0


        extFloatingButton.setOnClickListener {
            keyboardEditText.requestFocus()
            showSoftKeyboard(keyboardEditText)
        }

        keyboardEditText.setOnKeyListener { _ , keyCode, event ->
            Log.d("KeyboardEditText", "event: $event")

            val msg = Message.obtain(mBackgroundHandler)
            val instructions = Instructions(
                instructionType = Instructions.InstructionType.OP_TYPING,
                actionType = Instructions.ActionType.fromInt(event.action),
                input = getCharacterDisplayLabel(event)
            )
            msg.obj = processCommand(instructions)
            msg.sendToTarget()
            super.onKeyUp(keyCode, event)

            true
        }

        layout.setOnTouchListener { _, ev ->
            hideSoftKeyboard()
            Log.i("OnTouch", "$ev")
            return@setOnTouchListener true
        }
    }

    private fun showSoftKeyboard(editText: EditText) {
        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT)
    }

    private fun hideSoftKeyboard() {
        imm.hideSoftInputFromWindow( extFloatingButton.applicationWindowToken, 0)
    }

    override fun onDestroy() {
        super.onDestroy()
        es.shutdownNow()
        mBackgroundHandlerThread.quitSafely()
    }

    override fun onPause() {
        super.onPause()
        hideSoftKeyboard()
    }

    private fun processCommand(instructions: Instructions): String? {
        val gson = Gson()
        return gson.toJson(instructions, instructions::class.java)
    }

    private fun connect(){
        sock = Socket("192.168.1.80", 5555)
        sock.keepAlive = true
        writeToServer = PrintWriter(OutputStreamWriter(sock.getOutputStream(), StandardCharsets.US_ASCII))
    }

    private fun getCharacterDisplayLabel(event: KeyEvent): String{
        return when(event.keyCode){
            KEYCODE_SPACE -> "SPACE"
            KEYCODE_SHIFT_LEFT -> "LSHIFT"
            KEYCODE_DEL -> "DEL"
            KEYCODE_APOSTROPHE -> "APOS"
            else -> event.keyCharacterMap.getDisplayLabel(event.keyCode).toString()
        }
    }
}