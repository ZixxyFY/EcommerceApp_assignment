package com.example.ecom_assignment

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object CreateAccount : Screen("create_account")
    object ForgotPassword : Screen("forgot_password")
    object Home : Screen("home")
    object Password : Screen("password/{email}") {
        fun createRoute(email: String) = "password/$email"
    }
}
