package com.product.orderfromhere.view

sealed class Route(val route: String) {
    object Login : Route("login")
    object Register : Route("register")
    object Dashboard: Route(route = "dashboard")
}