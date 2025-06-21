package com.example.ecom_assignment

import android.widget.Toast // <<< ADD THIS IMPORT
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext // <<< ADD THIS IMPORT
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.ecom_assignment.ui.theme.PrimaryPurple
import com.example.ecom_assignment.ui.theme.TextBlack
import com.google.firebase.auth.FirebaseAuth
import com.example.ecom_assignment.ui.theme.InputField

@OptIn(ExperimentalMaterial3Api::class) // Needed for Material3 components if not already present
@Composable
fun PasswordScreen(navController: NavController, email: String) { // 'email' passed as NavArgument
    val context = LocalContext.current // <<< GET CONTEXT FOR TOASTS
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) } // <<< ADD LOADING STATE
    var errorMessage by remember { mutableStateOf<String?>(null) } // <<< USE FOR TOASTS
    val auth = FirebaseAuth.getInstance()

    // Observe errorMessage state to show a Toast
    LaunchedEffect(errorMessage) { // <<< ADD LAUNCHED EFFECT FOR ERROR MESSAGES
        errorMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            errorMessage = null // Clear the error after showing
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
            .padding(top = 56.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text("Sign in", style = MaterialTheme.typography.headlineLarge)
        Spacer(modifier = Modifier.height(32.dp))

        // Display the email being used for clarity (Optional but good UX)
        Text(
            text = "Signing in as: $email",
            style = MaterialTheme.typography.bodyLarge,
            color = TextBlack
        )
        Spacer(modifier = Modifier.height(16.dp))


        InputField(
            value = password,
            onValueChange = { password = it },
            placeholder = "Password",
            isPassword = true
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                if (password.isNotBlank()) { // Validate password is not blank
                    isLoading = true // <<< START LOADING
                    auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            isLoading = false // <<< STOP LOADING
                            if (task.isSuccessful) {
                                // Ensure this matches your actual Home screen route
                                navController.navigate(Screen.Home.route) {
                                    // Clear the back stack after successful login
                                    popUpTo(navController.graph.startDestinationId) { inclusive = true }
                                }
                            } else {
                                errorMessage = task.exception?.localizedMessage ?: "Sign in failed." // <<< USE errorMessage for Toast
                            }
                        }
                } else {
                    errorMessage = "Please enter your password." // <<< Prompt user for input
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(28.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple),
            enabled = !isLoading // <<< DISABLE BUTTON WHILE LOADING
        ) {
            Text("Continue", style = MaterialTheme.typography.titleLarge)
        }

        // REMOVED: errorMessage?.let { ... } as Toast will handle it now
        // Spacer(modifier = Modifier.height(12.dp))
        // Text(text = it, color = MaterialTheme.colorScheme.error)


        Spacer(modifier = Modifier.height(16.dp))

        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) { // <<< CENTER THE ROW
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

        if (isLoading) { // <<< SHOW PROGRESS INDICATOR WHEN LOADING
            Spacer(modifier = Modifier.height(16.dp))
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        }
    }
}