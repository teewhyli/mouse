package com.example.client

import android.os.Handler

sealed interface IConnection {
    fun connect(): Handler?
    fun send(message: String)
    fun disconnect()
}