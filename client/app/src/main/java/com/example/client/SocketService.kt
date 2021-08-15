package com.example.client

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import java.net.Socket

class SocketService: Service() {

    private val binder = SocketBinder()

//    override fun

    inner class SocketBinder : Binder() {
        // Return this instance of LocalService so clients can call public methods
        fun getService(): SocketService = this@SocketService
    }

    override fun onBind(intent: Intent?): IBinder? {
//        Toast.makeText(applicationContext, "binding", Toast.LENGTH_SHORT).show()
        Log.i("SOCKETSERVICE", "binding!")
        return null
    }
}