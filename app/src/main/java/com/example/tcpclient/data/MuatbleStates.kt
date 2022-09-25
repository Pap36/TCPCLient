package com.example.tcpclient.data

import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject
import javax.inject.Singleton

data class MutableSates(
    val permissionsGranted: Boolean = false,
    val wifiEnabled: Boolean = false,
    val connected: Boolean = false,
    val connectionStatus: String = "Not connected",
    val displayMessage: String = "No message",
)

/**
 * Singleton class representing the mutable state flow for the data class.
 */
@Singleton
class DataFlow @Inject constructor() {

    private val _flow = MutableStateFlow(MutableSates())

    fun getFlow(): MutableStateFlow<MutableSates> {
        return _flow
    }

}