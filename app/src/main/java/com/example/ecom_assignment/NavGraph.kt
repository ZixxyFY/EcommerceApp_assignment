package com.example.ecom_assignment

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

import com.example.ecom_assignment.LoginScreen
import com.example.ecom_assignment.CreateAccountScreen
import com.example.ecom_assignment.ForgotPasswordScreen
import com.example.ecom_assignment.PasswordScreen
import com.example.ecom_assignment.LocationScreen
import com.example.ecom_assignment.LocationHistoryScreen

import com.example.ecom_assignment.ui.screens.HomeScreen

@Composable
fun AppNavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Screen.Login.route) {
        composable(Screen.Login.route) { LoginScreen(navController) }
        composable(Screen.CreateAccount.route) { CreateAccountScreen(navController) }
        composable(Screen.ForgotPassword.route) { ForgotPasswordScreen(navController) }
        composable(
            route = Screen.Password.route,
            arguments = listOf(navArgument("email") { type = NavType.StringType })
        ) { backStackEntry ->
            val email = backStackEntry.arguments?.getString("email").orEmpty()
            PasswordScreen(navController, email)
        }
        composable(Screen.Home.route) { HomeScreen(navController) }
        composable(Screen.Location.route) { LocationScreen(navController) }
        composable(Screen.LocationHistory.route) { LocationHistoryScreen(navController) }
        composable(Screen.PhoneAuth.route) { PhoneAuthScreen(navController) }
    }
}