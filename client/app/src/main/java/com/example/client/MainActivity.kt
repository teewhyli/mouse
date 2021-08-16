package com.example.client

import android.annotation.SuppressLint
import android.os.*
import android.view.KeyEvent
import android.view.KeyEvent.*
import android.view.View
import android.view.inputmethod.InputMethodManager
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


class MainActivity : AppCompatActivity(), Callback {

    private lateinit var layout: View
    private lateinit var extFloatingButton: ExtendedFloatingActionButton
    private lateinit var imm: InputMethodManager
    private var mBackgroundHandlerThread: HandlerThread = HandlerThread("Message Handler Thread")
    private val es: ExecutorService = Executors.newCachedThreadPool()
    private lateinit var mBackgroundHandler: Handler

    @SuppressLint("ClickableViewAccessibility") // we're not dealing with swipes.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
        imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager

        extFloatingButton.setOnClickListener { showSoftKeyboard() }

        layout.setOnTouchListener { _, _ ->
            hideSoftKeyboard()
            return@setOnTouchListener true
        }
    }

    private fun showSoftKeyboard() {
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0)
    }

    private fun hideSoftKeyboard() {
        imm.hideSoftInputFromWindow( extFloatingButton.applicationWindowToken, 0)
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent): Boolean {
        if (event.isShiftPressed){
            println("shifted")
        }
        return when (keyCode) {
            KEYCODE_SHIFT_LEFT -> { true }
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