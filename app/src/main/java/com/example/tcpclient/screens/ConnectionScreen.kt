package com.example.tcpclient.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.tcpclient.viewmodels.ConnectionViewModel

@Composable
fun ConnectionScreen(navController: NavHostController) {

    val connectionViewModel = hiltViewModel<ConnectionViewModel>()
    val stateFlow by connectionViewModel.stateFlow.collectAsState()

    BackHandler {
        if (stateFlow.connected) connectionViewModel.sendMessage("bye")
        connectionViewModel.disconnect()
        navController.popBackStack()
    }

    if (stateFlow.connected) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "Last message from server:")
            Spacer(modifier = Modifier.height(16.dp))
            Text(stateFlow.displayMessage)
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                connectionViewModel.sendMessage("Hello from client")
            }) {
                Text(text = "Send message to server")
            }
        }
    } else if (stateFlow.displayMessage == "Disconnected from server" ||
        stateFlow.displayMessage == "Connection failed") {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = stateFlow.displayMessage)
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { connectionViewModel.reconnect() }) {
                Text(text = "Try to Reconnect")
            }
        }
    } else {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CircularProgressIndicator()
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Connecting to Server")
        }
    }

}