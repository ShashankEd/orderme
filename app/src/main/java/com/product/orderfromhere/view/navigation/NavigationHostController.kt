package com.product.orderfromhere.view.navigation

import android.annotation.SuppressLint
import android.app.Application
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.product.orderfromhere.view.DashboardScreen
import com.product.orderfromhere.view.LoginScreen
import com.product.orderfromhere.view.RegisterScreen
import com.product.orderfromhere.view.SplashScreen
import com.product.orderfromhere.viewmodel.ApolloViewModel
import com.product.orderfromhere.viewmodel.LoginViewModel

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("ViewModelConstructorInComposable", "UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun RootNav(application: Application) {
    val loginViewModel = LoginViewModel(application)
    val apolloViewModel = ApolloViewModel()
    val navHostController = rememberNavController()
    val currentRoute = navHostController.currentBackStackEntryAsState().value?.destination?.route

    // Conditionally show Scaffold for home routes
    val showHomeScaffold = currentRoute?.startsWith(ScreenRoutes.HomeNav.route) == true

    if (showHomeScaffold) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Dashboard") },
                    modifier = Modifier.statusBarsPadding(),
                )
            }
        ) {
            NavHost(
                navController = navHostController,
                startDestination = ScreenRoutes.AuthNav.route,
            ) {
                AuthNav(navHostController, apolloViewModel, loginViewModel)
                HomeNav(navHostController, apolloViewModel, loginViewModel)
            }
        }
    } else {
        NavHost(
            navController = navHostController,
            startDestination = ScreenRoutes.AuthNav.route,
        ) {
            AuthNav(navHostController, apolloViewModel, loginViewModel)
            HomeNav(navHostController, apolloViewModel, loginViewModel)
        }
    }
}

fun NavGraphBuilder.HomeNav(
    navHostController: NavHostController,
    apolloViewModel: ApolloViewModel,
    loginViewModel: LoginViewModel
) {
    navigation(
        route = ScreenRoutes.HomeNav.route,
        startDestination = ScreenRoutes.DashboardScreen.route
    ) {
        composable(route = ScreenRoutes.DashboardScreen.route) {
            DashboardScreen(
                viewModel = loginViewModel,
                navController = navHostController,
                apolloViewModel = apolloViewModel
            )
        }
        composable(route = ScreenRoutes.SettingsScreen.route) {
            SettingScreen(
                navHostController = navHostController,
                loginViewModel = loginViewModel
            )
        }
    }
}

fun NavGraphBuilder.AuthNav(
    navHostController: NavHostController,
    apolloViewModel: ApolloViewModel,
    loginViewModel: LoginViewModel
) {
    navigation(
        startDestination = ScreenRoutes.SplashScreen.route,
        route = ScreenRoutes.AuthNav.route,
    )
    {
        composable( ScreenRoutes.SplashScreen.route) {
            SplashScreen(navHostController, loginViewModel, apolloViewModel)
        }

        composable( ScreenRoutes.RegisterScreen.route) {
            RegisterScreen(loginViewModel, navHostController, apolloViewModel)
        }

        composable( ScreenRoutes.LoginScreen.route) {
            LoginScreen(loginViewModel, navHostController, apolloViewModel)
        }
    }
}