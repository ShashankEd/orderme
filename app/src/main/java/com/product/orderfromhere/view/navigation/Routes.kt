package com.product.orderfromhere.view.navigation

sealed class ScreenRoutes(val route : String) {
    //Screen Routes
    data object SplashScreen : ScreenRoutes("splash_screen")

    data object LoginScreen : ScreenRoutes("login_screen")

    data object RegisterScreen : ScreenRoutes("register_screen")

    data object DashboardScreen : ScreenRoutes("dashboard_screen")

    data object SettingsScreen : ScreenRoutes("settings_screen")

    //Graph Routes
    data object AuthNav : ScreenRoutes("AUTH_NAV_GRAPH")

    data object HomeNav : ScreenRoutes("HOME_NAV_GRAPH")
}