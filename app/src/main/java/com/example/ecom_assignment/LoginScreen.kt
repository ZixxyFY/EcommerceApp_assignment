
package com.example.ecom_assignment

import android.app.Activity
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.ecom_assignment.ui.theme.InputField
import com.example.ecom_assignment.ui.theme.SocialLoginButton
import com.example.ecom_assignment.ui.theme.PrimaryPurple
import com.example.ecom_assignment.ui.theme.TextBlack
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavController) {
    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val oneTapClient = remember { Identity.getSignInClient(context) }
    val webClientId = stringResource(R.string.web_client_id)
    val signInRequest = remember(webClientId) {
        BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setServerClientId(webClientId)
                    .setFilterByAuthorizedAccounts(false)
                    .build()
            )
            .setAutoSelectEnabled(false)
            .build()
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult()
    ) { result ->
        isLoading = false
        if (result.resultCode == Activity.RESULT_OK) {
            try {
                val credential = oneTapClient.getSignInCredentialFromIntent(result.data)
                val idToken = credential.googleIdToken
                if (idToken != null) {
                    val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
                    auth.signInWithCredential(firebaseCredential)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                navController.navigate(Screen.Home.route) {
                                    popUpTo(navController.graph.startDestinationId) { inclusive = true }
                                }
                            } else {
                                errorMessage = task.exception?.localizedMessage ?: "Firebase sign-in failed."
                            }
                        }
                } else {
                    errorMessage = "ID token was null."
                }
            } catch (e: Exception) {
                errorMessage = "Google Sign-In failed: ${e.localizedMessage}"
            }
        } else {
            errorMessage = "Google Sign-In canceled or failed."
        }
    }

    LaunchedEffect(errorMessage) {
        errorMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            errorMessage = null
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 56.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text("Sign in", style = MaterialTheme.typography.headlineLarge)
        Spacer(modifier = Modifier.height(32.dp))

        InputField(value = email, onValueChange = { email = it }, placeholder = "Email Address")
        Spacer(modifier = Modifier.height(16.dp))
        InputField(value = password, onValueChange = { password = it }, placeholder = "Password", isPassword = true)
        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                if (email.isNotBlank() && password.isNotBlank()) {
                    isLoading = true
                    auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            isLoading = false
                            if (task.isSuccessful) {
                                navController.navigate(Screen.Home.route) {
                                    popUpTo(navController.graph.startDestinationId) { inclusive = true }
                                }
                            } else {
                                errorMessage = task.exception?.localizedMessage ?: "Error signing in."
                            }
                        }
                } else {
                    errorMessage = "Please enter email and password."
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple),
            shape = RoundedCornerShape(28.dp)
        ) {
            Text("Sign In with Email", style = MaterialTheme.typography.titleLarge)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text("Don't have an account?", style = MaterialTheme.typography.bodySmall)
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                "Create one",
                modifier = Modifier.clickable { navController.navigate(Screen.CreateAccount.route) },
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold, color = TextBlack)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        SocialLoginButton(
            iconRes = R.drawable.ic_google,
            text = "Continue with Google",
            onClick = {
                isLoading = true
                oneTapClient.beginSignIn(signInRequest)
                    .addOnSuccessListener {
                        launcher.launch(IntentSenderRequest.Builder(it.pendingIntent.intentSender).build())
                    }
                    .addOnFailureListener {
                        isLoading = false
                        errorMessage = "Google One Tap Sign-In failed: ${it.localizedMessage}"
                    }
            }
        )

        Spacer(modifier = Modifier.height(12.dp))

        SocialLoginButton(
            iconRes = R.drawable.ic_apple,
            text = "Continue with Apple",
            onClick = {
                Toast.makeText(context, "Apple Sign-In not implemented.", Toast.LENGTH_SHORT).show()
            }
        )

        Spacer(modifier = Modifier.height(12.dp))

        SocialLoginButton(
            iconRes = R.drawable.ic_facebook,
            text = "Continue with Facebook",
            onClick = {
                Toast.makeText(context, "Facebook Sign-In not implemented.", Toast.LENGTH_SHORT).show()
            }
        )

        if (isLoading) {
            Spacer(modifier = Modifier.height(12.dp))
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        }
    }
}
