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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.product.orderfromhere.view.DashboardScreen
import com.product.orderfromhere.view.LoginScreen
import com.product.orderfromhere.view.RegisterScreen
import com.product.orderfromhere.view.SplashScreen
import com.product.orderfromhere.viewmodel.ApolloViewModel
import com.product.orderfromhere.viewmodel.LoginViewModel

@SuppressLint("ViewModelConstructorInComposable")
@Composable
fun AppNavigation(application: Application) {
    val navHostController = rememberNavController()
    val loginViewModel = LoginViewModel(application)
//    val apolloViewModel = ApolloViewModel()

    NavHost(
        navController = navHostController,
        startDestination = SplashRoute.Splash.route,
        route = "root_graph"
    ) {
        //splash route
        composable(SplashRoute.Splash.route) {
            SplashScreen(navHostController, loginViewModel)
        }
        navigation(
            startDestination = AuthRoute.Login.route,
            route = "auth_graph",
        )
        {
            val apolloViewModel = ApolloViewModel()
            apolloViewModel.updateEndpoint("http://10.0.2.2:8000/graphql/authenticate")
            composable(AuthRoute.Register.route) {
                RegisterScreen(loginViewModel, navHostController, apolloViewModel)
            }

            composable(AuthRoute.Login.route) {
                LoginScreen(loginViewModel, navHostController, apolloViewModel)
            }
        }

        navigation(
            startDestination = AppRoute.Dashboard.route,
            route = "app_graph"
        ) {
            val apolloViewModel = ApolloViewModel()
            apolloViewModel.updateEndpoint("http://10.0.2.2:8000/graphql/other")
            composable(AppRoute.Dashboard.route)
            {
//                MainScreenWithBottomTab(navHostController, loginViewModel, sessionToken)
                DashboardScreen(
                    loginViewModel,
                    navHostController,
                    apolloViewModel
                )
            }

            composable(AppRoute.Settings.route)
            {
                SettingScreen(navHostController, loginViewModel)
            }
        }

    }
}

@SuppressLint("ViewModelConstructorInComposable")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreenWithBottomTab(rootNavController: NavHostController, loginViewModel: LoginViewModel) {
    // This is the second, internal NavController
    val mainNavController = rememberNavController()
    val apolloViewModel = ApolloViewModel()
    apolloViewModel.updateEndpoint("http://10.0.2.2:8000/graphql/other")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Dashboard") },
                modifier = Modifier.statusBarsPadding(),
            )
        }
    ) { innerPadding ->
        NavHost(
            navController = mainNavController, // Use the internal NavController
            startDestination = AppRoute.Dashboard.route,
            route = "app_graph",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(AppRoute.Dashboard.route) {
//                DashboardScreen(
//                 loginViewModel,
//                    rootNavController,
//                    apolloViewModel
//                )
            }
        }
    }
}