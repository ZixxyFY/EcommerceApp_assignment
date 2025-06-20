package com.example.ecom_assignment

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.ecom_assignment.ui.theme.PrimaryPurple
import com.example.ecom_assignment.ui.theme.TextBlack
import com.google.firebase.auth.FirebaseAuth

@Composable
fun PasswordScreen(navController: NavController, email: String) {
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val auth = FirebaseAuth.getInstance()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
            .padding(top = 56.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text("Sign in", style = MaterialTheme.typography.headlineLarge)
        Spacer(modifier = Modifier.height(32.dp))

        InputField(
            value = password,
            onValueChange = { password = it },
            placeholder = "Password",
            isPassword = true
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // Ensure this matches your actual Home screen route
                            navController.navigate(Screen.Home.route) {
                                // Clear the back stack for email/password login too
                                popUpTo(navController.graph.startDestinationId) { inclusive = true }
                            }
                        } else {
                            errorMessage = task.exception?.message
                        }
                    }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(28.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple)
        ) {
            Text("Continue", style = MaterialTheme.typography.titleLarge)
        }

        errorMessage?.let {
            Spacer(modifier = Modifier.height(12.dp))
            Text(text = it, color = MaterialTheme.colorScheme.error)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "Forgot Password?",
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Normal, color = TextBlack)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "Reset",
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold, color = TextBlack),
                modifier = Modifier.clickable {
                    navController.navigate(Screen.ForgotPassword.route)
                }
            )
        }
    }
}