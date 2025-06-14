package com.example.ecom_assign

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object CreateAccount : Screen("create_account")
    object Password : Screen("password")
    object ForgotPassword : Screen("forgot_password")
}