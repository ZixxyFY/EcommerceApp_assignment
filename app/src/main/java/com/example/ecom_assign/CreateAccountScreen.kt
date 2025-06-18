// app/src/main/java/com/example/ecom_assign/CreateAccountScreen.kt
package com.example.ecom_assign

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
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.example.ecom_assign.ui.theme.BackgroundLightGray
import com.example.ecom_assign.ui.theme.PrimaryPurple
import com.example.ecom_assign.ui.theme.TextBlack
import com.example.ecom_assign.ui.theme.TextGrayPlaceholder

@Composable
fun CreateAccountScreen(navController: NavController) {
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
            .padding(top = 56.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            "Create Account",
            style = MaterialTheme.typography.headlineLarge
        )

        Spacer(modifier = Modifier.height(32.dp))

        InputField(firstName, { firstName = it }, "Firstname", false)
        Spacer(modifier = Modifier.height(16.dp))
        InputField(lastName, { lastName = it }, "Lastname", false)
        Spacer(modifier = Modifier.height(16.dp))
        InputField(email, { email = it }, "Email Address", false)
        Spacer(modifier = Modifier.height(16.dp))
        InputField(password, { password = it }, "Password", isPassword = true)

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                navController.navigate(Screen.Login.route)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(28.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple)
        ) {
            Text("Continue", style = MaterialTheme.typography.titleLarge)
        }

        Spacer(modifier = Modifier.height(24.dp))

        // --- MODIFIED SECTION: Removed Social Login Buttons ---
        // Previously:
        // SocialLoginButton(R.drawable.ic_apple, "Continue with Apple")
        // Spacer(modifier = Modifier.height(12.dp))
        // SocialLoginButton(R.drawable.ic_google, "Continue with Google")
        // Spacer(modifier = Modifier.height(12.dp))
        // SocialLoginButton(R.drawable.ic_facebook, "Continue with Facebook")
        // --- END MODIFIED SECTION ---

        // You might want to adjust this spacer if the removal of social buttons
        // makes the "Already have an account?" text too high or low.
        // For now, keeping it as is, might need more space if it's the last element.
        // Let's add a larger spacer if social buttons are removed, to push "Already have an account?" down
        Spacer(modifier = Modifier.height(48.dp)) // Increased spacer for visual balance

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Already have an account?",
                style = MaterialTheme.typography.bodySmall
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "Sign in",
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold, color = TextBlack),
                modifier = Modifier.clickable {
                    navController.navigate(Screen.Login.route)
                }
            )
        }
    }
}

// InputField and SocialLoginButton are now commonly used.
// It's a good practice to move these reusable Composables to a separate file,
// e.g., `app/src/main/java/com/example/ecom_assign/ui/components/CommonComposables.kt`
// if they are used in many screens. For now, keeping them in CreateAccountScreen.kt
// as they were previously, but be mindful of their reuse.

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    isPassword: Boolean
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = {
            Text(
                text = placeholder,
                color = TextGrayPlaceholder,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Normal)
            )
        },
        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = RoundedCornerShape(8.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = BackgroundLightGray,
            unfocusedContainerColor = BackgroundLightGray,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            errorIndicatorColor = Color.Transparent
        ),
        textStyle = MaterialTheme.typography.bodyLarge.copy(color = TextBlack)
    )
}

// This composable might be in CreateAccountScreen.kt or a separate file like CommonComposables.kt
@Composable
fun SocialLoginButton(iconRes: Int, text: String, onClick: () -> Unit) { // ADD onClick parameter
    Button(
        onClick = onClick, // Use the passed-in onClick lambda
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = RoundedCornerShape(28.dp),
        colors = ButtonDefaults.buttonColors(containerColor = BackgroundLightGray),
        elevation = ButtonDefaults.buttonElevation(0.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Image(
                painter = painterResource(id = iconRes),
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text,
                color = TextBlack,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Normal)
            )
        }
    }
}