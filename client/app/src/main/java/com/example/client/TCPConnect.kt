package com.example.client

import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import java.io.OutputStreamWriter
import java.io.PrintWriter
import java.net.Socket
import java.nio.charset.StandardCharsets
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

object TCPConnect : IConnection {

    private lateinit var sock: Socket
    private lateinit var writeToServer: PrintWriter
    private var mBackgroundHandlerThread: HandlerThread = HandlerThread("Message Handler Thread")
    private val executorService: ExecutorService = Executors.newCachedThreadPool()
    private lateinit var mBackgroundHandler: Handler

    override fun connect(): Handler {
        executorService.submit {
            sock = Socket("192.168.1.83", 5555)
            sock.keepAlive = true
            writeToServer =
                PrintWriter(OutputStreamWriter(sock.getOutputStream(), StandardCharsets.US_ASCII))
        }

        mBackgroundHandlerThread.start()

        mBackgroundHandler = Handler(mBackgroundHandlerThread.looper) {
            val msg: String = it.obj.toString()

            Log.d("handler", msg)

            send(msg)

            true
        }

        return mBackgroundHandler
    }

    override fun send(message: String) {
        Log.d("handler", "send")
        writeToServer.println(message)
        writeToServer.flush()
    }

    override fun disconnect() {
        Log.d("handler", "disconnect")
        mBackgroundHandlerThread.quitSafely()
        executorService.shutdownNow()
    }
}