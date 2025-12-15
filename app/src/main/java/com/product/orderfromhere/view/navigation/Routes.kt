package com.product.orderfromhere.view.navigation

// Auth screens
sealed class AuthRoute(val route: String) {
    object Login : AuthRoute("login")
    object Register : AuthRoute("register")
}

// App screens
sealed class AppRoute(val route: String) {
    object Dashboard: AppRoute(route = "dashboard")
    object Settings: AppRoute(route = "settings")
    object Feedback: AppRoute(route = "feedback")
}