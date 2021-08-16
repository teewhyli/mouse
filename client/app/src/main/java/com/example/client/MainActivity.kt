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
    private var mBackgroundHandlerThread: HandlerThread = HandlerThread("Message Handler Thread")
    private val es: ExecutorService = Executors.newCachedThreadPool()
    private lateinit var mBackgroundHandler: Handler

    @SuppressLint("ClickableViewAccessibility") // we're not dealing with swipes.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager

        mBackgroundHandlerThread.start()

        mBackgroundHandler = Handler(mBackgroundHandlerThread.looper){
            val msg: String = it.obj.toString()
            es.submit{
                val socket = Socket("192.168.1.80", 5555)
                socket.use { sock ->
                    val writeToServer =
                        PrintWriter(OutputStreamWriter(sock.getOutputStream(), StandardCharsets.UTF_8))

                    writeToServer.use { write ->
                        write.print(msg)
                        write.flush()
                    }
                }
            }
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

        keyboardEditText.setOnKeyListener { v, keyCode, event ->
            Log.d("KeyboardEditText", "event: $event")

            if (event.action == ACTION_UP) {
                when (keyCode) {
                    KEYCODE_SHIFT_LEFT -> {
                        true
                    }
                    else -> {
                        val msg = Message.obtain(mBackgroundHandler)
                        val instructions = Instructions()
                        instructions.operationKind = Instructions.CommandType.OP_TYPING
                        instructions.inputStr = keyCode.toString()
                        msg.obj = processCommand(instructions)
                        msg.sendToTarget()
                        super.onKeyUp(keyCode, event)
                    }
                }
            }

            true
        }

        layout.setOnTouchListener { v, ev ->
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
}