package com.example.client

import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import java.io.OutputStreamWriter
import java.io.PrintWriter
import java.net.Socket
import java.nio.charset.StandardCharsets
import java.util.concurrent.CompletableFuture
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

object TCP {

    private var TAG = "TCP"
    private lateinit var writeToServer: PrintWriter
    private var mBackgroundHandlerThread: HandlerThread = HandlerThread("Message Handler Thread")
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    var handler: Handler? = null

    init {
        mBackgroundHandlerThread.start()
        handler = Handler(mBackgroundHandlerThread.looper) {
            Log.d(TAG, "Handler activated")

            it.obj.also { msg -> send(msg.toString()) }
            true
        }
    }

    fun connect(host: String, port: Int): CompletableFuture<Socket> {

        return CompletableFuture.supplyAsync({
            Socket(host, port).apply {
                keepAlive = true
                writeToServer = PrintWriter(OutputStreamWriter(
                    this.getOutputStream(),
                    StandardCharsets.US_ASCII)
                )
            } }, executorService)
    }

    private fun send(message: String) {
        Log.d("handler", "send")

        writeToServer.run {
            this.println(message)
            flush()
        }
    }

    fun disconnect() {
        Log.d("handler", "disconnect")
        mBackgroundHandlerThread.quitSafely()
    }
}