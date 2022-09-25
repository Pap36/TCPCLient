package com.example.tcpclient.viewmodels

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tcpclient.data.DataFlow
import com.example.tcpclient.data.sendMessageToServer
import com.example.tcpclient.data.startListening
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.asStateFlow
import java.io.InputStream
import java.io.OutputStream
import java.net.InetSocketAddress
import java.net.Socket
import java.net.SocketAddress
import javax.inject.Inject

@HiltViewModel
class ConnectionViewModel @Inject constructor(
    private val dataFlow: DataFlow,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val iPAddress = savedStateHandle.get<String>("IPaddress")
    val stateFlow =  dataFlow.getFlow().asStateFlow()
    private var outputStream: OutputStream? = null
    private var socket: Socket? = null
    private var inputStream: InputStream? = null
    private var connectionJob: Job? = null

    fun reconnect(){
        dataFlow.getFlow().value = dataFlow.getFlow().value.copy(
            connectionStatus = "Not connected",
            connected = false,
            displayMessage = "Reconnecting..."
        )
        connectToServer(iPAddress!!)
    }

    fun disconnect(){
        try {
            dataFlow.getFlow().value = dataFlow.getFlow().value.copy(
                connectionStatus = "Not connected"
            )
            socket?.close()
            connectionJob?.cancel()
        } catch (e: Exception) {
            Log.e("ConnectionViewModel", "disconnect: $e")
        }
    }

    private fun connectToServer(address: String) {
        if (dataFlow.getFlow().value.connectionStatus == "Not connected")
        {
            connectionJob = viewModelScope.launch(Dispatchers.IO) {
                try {
                    dataFlow.getFlow().value = dataFlow.getFlow().value.copy(
                        connectionStatus = "Connecting"
                    )

                    // log the address
                    Log.d("ConnectionViewModel", "Connecting to $address")
                    val socketAddress: SocketAddress = InetSocketAddress(address, 9090)
                    socket = Socket()
                    socket!!.connect(socketAddress, 300)

                    yield()

                    inputStream = socket!!.getInputStream()
                    outputStream = socket!!.getOutputStream()

                    val onMessageReceived: (String) -> Unit = { message ->
                        when (message) {
                            "Connected" -> {
                                dataFlow.getFlow().value =
                                    dataFlow.getFlow().value.copy(
                                        connectionStatus = "Connected",
                                        connected = true,
                                        displayMessage = "Connected to server"
                                    )
                            }
                            "bye" -> {
                                dataFlow.getFlow().value =
                                    dataFlow.getFlow().value.copy(
                                        connectionStatus = "Not connected",
                                        connected = false,
                                        displayMessage = "Disconnected from server"
                                    )
                                inputStream!!.close()
                                outputStream!!.close()
                                socket!!.close()
                            }
                            else -> {
                                dataFlow.getFlow().value =
                                    dataFlow.getFlow().value.copy(
                                        displayMessage = message
                                    )
                            }
                        }
                    }

                    startListening(inputStream!!, this, onMessageReceived)

                } catch (e: Exception) {
                    dataFlow.getFlow().value = dataFlow.getFlow().value.copy(
                        connectionStatus = "Not connected",
                        displayMessage = "Connection failed"
                    )
                    Log.d("ConnectionViewModel", "Failed to connect to server")
                    Log.d("ConnectionViewModel", e.toString())
                }
            }
        }
    }

    fun sendMessage(message: String) {
        if (message == "bye") {
            dataFlow.getFlow().value = dataFlow.getFlow().value.copy(
                connectionStatus = "Not connected",
                connected = false,
                displayMessage = "Disconnected from server"
            )
            inputStream!!.close()
            outputStream!!.close()
            socket!!.close()
        }
        viewModelScope.launch(Dispatchers.IO) {
            sendMessageToServer(message, outputStream!!)
        }
    }

    init {
        connectToServer(iPAddress!!)
    }


}