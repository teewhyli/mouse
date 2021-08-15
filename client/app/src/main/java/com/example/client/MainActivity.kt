package com.example.client

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.*
import android.util.Log
import android.view.KeyEvent
import android.view.KeyEvent.*
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.*
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import java.io.*
import java.net.Socket
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.properties.Delegates


class MainActivity : AppCompatActivity(), Callback {

    private lateinit var layout: View
    private lateinit var extFloatingButton: ExtendedFloatingActionButton
    private lateinit var imm: InputMethodManager
    private lateinit var mBackgroundHandler: Handler
    private lateinit var mSocketService: SocketService
    private var mBound: Boolean = false

    private val mConnection = object : ServiceConnection {
        // Called when the connection with the service is established
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            // Because we have bound to an explicit
            // service that is running in our own process, we can
            // cast its IBinder to a concrete class and directly access it.
            val binder = service as SocketService.SocketBinder
            mSocketService = binder.getService()
            mBound = true
        }

        // Called when the connection with the service disconnects unexpectedly
        override fun onServiceDisconnected(className: ComponentName) {
            mBound = false
        }
    }

    @SuppressLint("ClickableViewAccessibility") // we're not dealing with swipes.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mHandlerThread = HandlerThread("hehe")
        mHandlerThread.start()
        mBackgroundHandler = Handler(mHandlerThread.looper){
            println(it.arg1);
            true
        }

//        val manager = super.getApplicationContext().getSystemService(WIFI_SERVICE) as WifiManager
//        val ipAddress = manager.connectionInfo.ipAddress
//        val address: String = InetAddress.getByAddress(
//            ByteBuffer.allocate(4)
//                .order(ByteOrder.LITTLE_ENDIAN)
//                .putInt(ipAddress).array())
//            .hostAddress
//        println(address)


        val es: ExecutorService = Executors.newCachedThreadPool()
        es.submit {
            try {
                    System.err.println("connected to server; type a line to see it echoed, type quit to exit")
            } catch (ioe: IOException) {
                ioe.printStackTrace()
            }
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

    override fun onStart() {
        super.onStart()
        Log.i("MAIN_ACTIVITY", "inside on start")
        Intent(this, SocketService::class.java).also { intent ->
            bindService(intent, mConnection, Context.BIND_AUTO_CREATE)
        }
    }

    override fun onStop() {
        super.onStop()
        unbindService(mConnection)
        mBound = false
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
                val msg: Message = Message.obtain(mBackgroundHandler)
                msg.arg1 = keyCode
                msg.sendToTarget()
                super.onKeyUp(keyCode, event)
            }
        }
    }
}