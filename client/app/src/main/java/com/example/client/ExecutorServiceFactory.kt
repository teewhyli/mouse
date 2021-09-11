package com.example.client

import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class ExecutorServiceFactory {

    companion object{
        val executorService: ExecutorService = Executors.newCachedThreadPool()
    }
}