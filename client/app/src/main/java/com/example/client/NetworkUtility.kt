package com.example.client

import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import java.io.OutputStreamWriter
import java.io.PrintWriter
import java.net.Socket
import java.nio.charset.StandardCharsets

class NetworkUtility {

    companion object{

        private lateinit var sock: Socket
        private lateinit var writeToServer: PrintWriter
        private var mBackgroundHandlerThread: HandlerThread = HandlerThread("Message Handler Thread")
        lateinit var mBackgroundHandler: Handler

        fun quitHandlerThreadSafely() {
            mBackgroundHandlerThread.quitSafely()
        }

        fun connect() {
            ExecutorServiceFactory.executorService.submit {
                sock = Socket("192.168.1.83", 5555)
                sock.keepAlive = true
                writeToServer =
                    PrintWriter(OutputStreamWriter(sock.getOutputStream(), StandardCharsets.US_ASCII))
            }

            mBackgroundHandlerThread.start()

            mBackgroundHandler = Handler(mBackgroundHandlerThread.looper){
                val msg: String = it.obj.toString()

                Log.d("handler", msg)

                writeToServer.println(msg)
                writeToServer.flush()
                true
            }
        }
    }
}