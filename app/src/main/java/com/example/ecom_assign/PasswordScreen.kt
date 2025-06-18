// app/src/main/java/com/example/ecom_assign/PasswordScreen.kt
package com.example.ecom_assign

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.ecom_assign.ui.theme.BackgroundLightGray
import com.example.ecom_assign.ui.theme.PrimaryPurple
import com.example.ecom_assign.ui.theme.TextBlack // Make sure TextBlack is imported

@Composable
fun PasswordScreen(navController: NavController) {
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
            .padding(top = 56.dp),
        horizontalAlignment = Alignment.Start
    ) {
        // --- MODIFIED: Screen Title ---
        Text(
            "Sign in", // Changed from "Enter Password"
            style = MaterialTheme.typography.headlineLarge
        )
        // --- END MODIFIED ---

        Spacer(modifier = Modifier.height(32.dp)) // Adjusted spacing after title

        // InputField remains the same
        InputField(
            value = password,
            onValueChange = { password = it },
            placeholder = "Password",
            isPassword = true
        )

        Spacer(modifier = Modifier.height(24.dp))

        // --- MODIFIED: Button Text ---
        Button(
            onClick = { /* Handle login - navigate to main app */ },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(28.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple)
        ) {
            Text("Continue", style = MaterialTheme.typography.titleLarge) // Changed from "Sign In"
        }
        // --- END MODIFIED ---

        Spacer(modifier = Modifier.height(16.dp))

        // --- MODIFIED: "Forgot Password? Reset" Link ---
        Row(
            // Removed horizontalArrangement.Center to default to start alignment
            modifier = Modifier.fillMaxWidth(), // Fill width to respect horizontal padding
            verticalAlignment = Alignment.CenterVertically // For vertical alignment if texts have different heights
        ) {
            Text(
                text = "Forgot Password?",
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Normal, color = TextBlack) // Normal weight for "Forgot Password?"
            )
            Spacer(modifier = Modifier.width(4.dp)) // Small space between the two text parts
            Text(
                text = "Reset",
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold, color = TextBlack), // Bold and black for "Reset"
                modifier = Modifier.clickable {
                    navController.navigate(Screen.ForgotPassword.route) // "Reset" part is reactive
                }
            )
        }
        // --- END MODIFIED ---
    }
}