package com.example.tcpclient.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.tcpclient.viewmodels.HomeViewModel

@Composable
fun NavigationScreen() {

    val homeViewModel = hiltViewModel<HomeViewModel>()
    val stateFlow = homeViewModel.stateFlow.collectAsState()

    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "enable_wifi") {
        composable("enable_wifi") {
            EnableWifiScreen(homeViewModel = homeViewModel)
        }
        composable("connect") {
            ConnectToServerScreen(navController)
        }
        composable("connection/{IPaddress}") { navBackStackEntry ->
            navBackStackEntry.arguments?.getString("IPaddress")?.let { ipAddress ->
                ConnectionScreen(navController)
            }
        }
    }

    // logic handling the navigation
    if(stateFlow.value.permissionsGranted){
        if (!stateFlow.value.wifiEnabled) {
            val currentRoute = navController.currentBackStackEntry?.destination?.route
            // if there is no bluetooth, then go to EnableWifiScreen provided we are not already there
            if (currentRoute != "enable_wifi") {
                navController.navigate("enable_wifi") {
                    // make sure to clear the navigation stack so that a back button won't take us
                    // back to an undesired screen
                    popUpTo("connect") { inclusive = true }
                }
            }
        } else{
            val currentRoute = navController.currentBackStackEntry?.destination?.route
            if (currentRoute == "enable_wifi") {
                navController.navigate("connect") {
                    // same reasoning as above
                    popUpTo("enable_wifi") { inclusive = true }
                }
            }
        }
    }

}