package com.example.client

import java.lang.Exception

interface IConnectionFactory {
    enum class Type { TCP, UDP, Undefined }
    fun get(type: Type): IConnection
}

class ConnectionFactory: IConnectionFactory {
    override fun get(type: IConnectionFactory.Type): IConnection {
        return when (type) {
            IConnectionFactory.Type.TCP -> TCPConnect
            IConnectionFactory.Type.UDP -> TCPConnect
            else -> throw Exception("Unknown connection type, don't know how to create it")
        }
    }
}