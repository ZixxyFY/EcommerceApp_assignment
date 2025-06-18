// app/src/main/java/com/example/ecom_assign/LoginScreen.kt
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.ecom_assign.ui.theme.BackgroundLightGray
import com.example.ecom_assign.ui.theme.PrimaryPurple
import com.example.ecom_assign.ui.theme.TextBlack
import com.example.ecom_assign.ui.theme.TextGrayPlaceholder
// If SocialLoginButton is in a common file, you'll need to import it, e.g.:
// import com.example.ecom_assign.ui.components.SocialLoginButton // Adjust path as needed

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavController) {
    var email by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
            .padding(top = 56.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            "Sign in",
            style = MaterialTheme.typography.headlineLarge
        )

        Spacer(modifier = Modifier.height(32.dp))

        InputField(
            value = email,
            onValueChange = { email = it },
            placeholder = "Email Address",
            isPassword = false
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Main "Continue" button: Leads to PasswordScreen
        Button(
            onClick = { navController.navigate(Screen.Password.route) }, // Leads to PasswordScreen
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple),
            shape = RoundedCornerShape(28.dp)
        ) {
            Text("Continue", style = MaterialTheme.typography.titleLarge)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Don't have an Account?",
                style = MaterialTheme.typography.bodySmall
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "Create one",
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold, color = TextBlack),
                modifier = Modifier.clickable {
                    navController.navigate(Screen.CreateAccount.route)
                }
            )
        }

        Spacer(modifier = Modifier.height(32.dp))
        SocialLoginButton(
            iconRes = R.drawable.ic_apple,
            text = "Continue with Apple",
            onClick = { navController.navigate(Screen.Password.route) } // Navigates to PasswordScreen
        )
        Spacer(modifier = Modifier.height(12.dp))
        SocialLoginButton(
            iconRes = R.drawable.ic_google,
            text = "Continue with Google",
            onClick = { navController.navigate(Screen.Password.route) } // Navigates to PasswordScreen
        )
        Spacer(modifier = Modifier.height(12.dp))
        SocialLoginButton(
            iconRes = R.drawable.ic_facebook,
            text = "Continue with Facebook",
            onClick = { navController.navigate(Screen.Password.route) } // Navigates to PasswordScreen
        )

    }
}

// Ensure InputField and SocialLoginButton are accessible from this file.
// If they were originally in CreateAccountScreen.kt, and you haven't moved them,
// you might need to move them to a more common location or re-declare them here
// if you choose not to move them.
