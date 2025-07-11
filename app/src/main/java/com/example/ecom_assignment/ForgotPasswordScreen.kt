// app/src/main/java/com/example/ecom_assignment/ForgotPassword.kt
package com.example.ecom_assignment

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext // Needed for Toast
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.ecom_assignment.ui.theme.PrimaryPurple
import com.example.ecom_assignment.ui.theme.TextBlack

// <<< ADD THIS IMPORT LINE >>>
import com.example.ecom_assignment.ui.theme.InputField
// <<< END ADDITION >>>

import com.google.firebase.auth.FirebaseAuth
import android.widget.Toast // Needed for Toast

@Composable
fun ForgotPasswordScreen(navController: NavController) {
    val context = LocalContext.current // Get context for Toast
    var email by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) } // Add loading state
    var errorMessage by remember { mutableStateOf<String?>(null) } // Add error message state

    // Display Toast messages for success/failure
    LaunchedEffect(errorMessage) {
        errorMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            errorMessage = null
        }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp) // Consistent horizontal padding
            .padding(top = 56.dp), // Consistent top padding
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            "Forgot Password",
            style = MaterialTheme.typography.headlineLarge // Apply headlineLarge style
        )

        Spacer(modifier = Modifier.height(32.dp)) // Spacing after title

        InputField(
            value = email,
            onValueChange = { email = it },
            placeholder = "Enter Email Address",
            isPassword = false
        )

        Spacer(modifier = Modifier.height(24.dp)) // Spacing before button

        Button(
            onClick = {
                if (email.isNotBlank()) {
                    isLoading = true // Start loading
                    FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                        .addOnSuccessListener {
                            isLoading = false // End loading
                            Toast.makeText(context, "Password reset email sent to $email", Toast.LENGTH_LONG).show()
                            navController.navigate(Screen.Login.route) { // Navigate back to login
                                popUpTo(Screen.Login.route) { inclusive = true }
                            }
                        }
                        .addOnFailureListener { e ->
                            isLoading = false // End loading
                            errorMessage = e.localizedMessage ?: "Failed to send reset email."
                        }
                } else {
                    errorMessage = "Please enter your email address."
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(28.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple), // Use themed primary color
            enabled = !isLoading // Disable button while loading
        ) {
            Text("Continue", style = MaterialTheme.typography.titleLarge) // Apply titleLarge style
        }

        Spacer(modifier = Modifier.height(16.dp)) // Spacing before "Back to Sign In"

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center, // Center the row
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Back to Sign In",
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold, color = TextBlack), // Apply bodySmall, bold, black
                modifier = Modifier.clickable {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }

        if (isLoading) {
            Spacer(modifier = Modifier.height(16.dp))
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        }
    }
}