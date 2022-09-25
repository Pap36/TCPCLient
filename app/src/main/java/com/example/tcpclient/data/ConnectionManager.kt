package com.example.tcpclient.data

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.InputStream
import java.io.OutputStream
import java.io.PrintWriter
import java.net.SocketException

fun startListening(
    stream: InputStream,
    scope: CoroutineScope,
    onMessageReceived: (String) -> Unit) {
    scope.launch(Dispatchers.IO) {

        try {
            val reader = BufferedReader(stream.reader())
            while (true) {
                Log.d("ConnectionManager", "Waiting for message")
                val message = reader.readLine()
                Log.d("ConnectionManager", "Received message: $message")
                if (message != null) onMessageReceived(message)
                if (message == "bye") {
                    stream.close()
                    this.cancel()
                }
            }
        } catch (exception: SocketException) {
            this.cancel()
        }
    }
}

fun sendMessageToServer(message: String, outputStream: OutputStream) {
    Log.d("ConnectionManager", "Sending message: $message")
    val printWriter = PrintWriter(outputStream, true)
    printWriter.println(message)
    if (message == "bye") outputStream.close()
}

