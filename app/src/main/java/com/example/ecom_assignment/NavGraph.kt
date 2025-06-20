package com.example.ecom_assignment

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.ecom_assignment.ui.screens.LoginScreen

@Composable
fun AppNavGraph(navController: NavHostController) {
    NavHost(navController, startDestination = Screen.Login.route) {
        composable(Screen.Login.route) {
            LoginScreen(navController)
        }
        composable(Screen.CreateAccount.route) {
            CreateAccountScreen(navController)
        }
        composable(Screen.ForgotPassword.route) {
            ForgotPasswordScreen(navController)
        }
        composable("home") {
            HomeScreen(navController)
        }
        composable(Screen.Password.route) { backStackEntry ->
            val email = backStackEntry.arguments?.getString("email") ?: ""
            PasswordScreen(navController, email)
        }
    }
}
