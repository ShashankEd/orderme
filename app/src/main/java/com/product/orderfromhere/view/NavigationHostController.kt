package com.product.orderfromhere.view

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.product.orderfromhere.model.User
import com.product.orderfromhere.viewmodel.ApolloViewModel
import com.product.orderfromhere.viewmodel.LoginViewModel

@SuppressLint("ViewModelConstructorInComposable")
@Composable
fun NavigationHostController(navController: NavHostController) {
    val loginViewModel = LoginViewModel()
    val apolloViewModel = ApolloViewModel()
    NavHost(
        navController = navController,
        startDestination = Route.Register.route,
    ) {
        apolloViewModel.updateEndpoint("http://10.0.2.2:8000/graphql/authenticate")
        composable(route = Route.Register.route) {
            RegisterScreen(loginViewModel, navController,apolloViewModel )
        }

        composable(route = Route.Login.route) {
            LoginScreen(loginViewModel, navController, apolloViewModel)
        }
        composable(route = Route.Dashboard.route) {
            DashboardScreen(loginViewModel, navController, apolloViewModel )
        }

    }
}