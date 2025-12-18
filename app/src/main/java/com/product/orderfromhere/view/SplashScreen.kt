package com.product.orderfromhere.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.product.orderfromhere.view.navigation.SplashRoute
import com.product.orderfromhere.viewmodel.LoginViewModel

@Composable
fun SplashScreen(
    navHostController: NavHostController,
    loginViewModel: LoginViewModel
) {
    // new way
    var uiState by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    val sessionToken = loginViewModel.sessionFromStore.collectAsState().value
    LaunchedEffect(sessionToken) {
        println("sessionToken = ${sessionToken.isNullOrEmpty()}")
        uiState = sessionToken
        isLoading = false
    }
    println("uiState = ${uiState}")

    when {
        isLoading -> {
            //show the spinner
            println("******Loading*****")
            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                CircularProgressIndicator()
            }
        }
        !uiState.isNullOrEmpty() -> {
            println("******Token present*****")
            navHostController.navigate("app_graph") {
                popUpTo(SplashRoute.Splash.route) { inclusive = true }
            }
        }
        uiState.isNullOrEmpty() -> {
            println("******Token not there*****")
            navHostController.navigate("auth_graph") {
                popUpTo(SplashRoute.Splash.route) { inclusive = true }
            }
        }
    }
}