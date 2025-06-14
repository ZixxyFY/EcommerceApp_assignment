package com.example.ecom_assign

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.ecom_assign.ui.theme.EcommerceApp_assignmentTheme
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EcommerceApp_assignmentTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppEntryPoint() // Entry point handling Splash and Login screens
                }
            }
        }
    }
}

@Composable
fun AppEntryPoint() {
    var showSplash by remember { mutableStateOf(true) }

    // Delay coroutine: waits 2 seconds before showing LoginScreen
    LaunchedEffect(Unit) {
        delay(2000L)
        showSplash = false
    }

    if (showSplash) {
        SplashScreen() // From SplashScreen.kt
    } else {
        LoginScreen() // From LoginScreen.kt
    }
}
