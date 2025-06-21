package com.example.ecom_assignment

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object CreateAccount : Screen("create_account")
    object ForgotPassword : Screen("forgot_password")
    object Password : Screen("password/{email}") {
        fun createRoute(email: String): String = "password/$email"
    }
    object Home : Screen("home")

    object PhoneAuth : Screen("phone_auth")
}