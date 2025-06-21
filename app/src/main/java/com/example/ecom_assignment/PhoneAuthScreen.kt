package com.example.ecom_assignment
import android.app.Activity
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.ecom_assignment.ui.theme.InputField
import com.example.ecom_assignment.ui.theme.PrimaryPurple
import com.example.ecom_assignment.ui.theme.TextBlack
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhoneAuthScreen(navController: NavController) {
    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()

    var phoneNumber by remember { mutableStateOf("") }
    var verificationId by remember { mutableStateOf<String?>(null) }
    var otpCode by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isCodeSent by remember { mutableStateOf(false) } // To toggle UI for phone input vs OTP input

    // Callback for phone verification states
    val callbacks = remember {
        object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                // This method is called automatically when the SMS code is automatically retrieved
                // (e.g., using Google Play Services' SMS Retriever API).
                isLoading = true
                auth.signInWithCredential(credential)
                    .addOnCompleteListener { task ->
                        isLoading = false
                        if (task.isSuccessful) {
                            Toast.makeText(context, "Phone verification successful!", Toast.LENGTH_SHORT).show()
                            navController.navigate(Screen.Home.route) {
                                popUpTo(navController.graph.startDestinationId) { inclusive = true }
                            }
                        } else {
                            errorMessage = task.exception?.localizedMessage ?: "Phone sign-in failed."
                        }
                    }
            }

            override fun onVerificationFailed(e: FirebaseException) {
                isLoading = false
                errorMessage = e.localizedMessage ?: "Phone verification failed."
            }

            override fun onCodeSent(
                id: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                // The SMS verification code has been sent to the user's phone.
                verificationId = id
                isCodeSent = true
                isLoading = false
                Toast.makeText(context, "Verification code sent to $phoneNumber", Toast.LENGTH_LONG).show()
            }
        }
    }

    // Display error messages as Toasts
    LaunchedEffect(errorMessage) {
        errorMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            errorMessage = null
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
            .padding(top = 56.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text("Phone Sign In", style = MaterialTheme.typography.headlineLarge)
        Spacer(modifier = Modifier.height(32.dp))

        if (!isCodeSent) {
            // UI for entering phone number
            Text(
                "Enter your phone number to receive a verification code.",
                style = MaterialTheme.typography.bodyMedium,
                color = TextBlack
            )
            Spacer(modifier = Modifier.height(16.dp))

            InputField(
                value = phoneNumber,
                onValueChange = { phoneNumber = it },
                placeholder = "Phone Number (e.g., +1234567890)",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (phoneNumber.isNotBlank()) {
                        isLoading = true
                        PhoneAuthProvider.verifyPhoneNumber(
                            PhoneAuthOptions.newBuilder(auth)
                                .setPhoneNumber(phoneNumber) // Phone number to verify
                                .setTimeout(60L, TimeUnit.SECONDS) // Timeout duration
                                .setActivity(context as Activity) // Activity (context)
                                .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
                                .build()
                        )
                    } else {
                        errorMessage = "Please enter your phone number."
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple),
                enabled = !isLoading
            ) {
                Text("Send Code", style = MaterialTheme.typography.titleLarge)
            }
        } else {
            // UI for entering OTP
            Text(
                "Enter the 6-digit code sent to $phoneNumber",
                style = MaterialTheme.typography.bodyMedium,
                color = TextBlack
            )
            Spacer(modifier = Modifier.height(16.dp))

            InputField(
                value = otpCode,
                onValueChange = { if (it.length <= 6) otpCode = it }, // Limit OTP to 6 digits
                placeholder = "Verification Code",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (otpCode.length == 6 && verificationId != null) {
                        isLoading = true
                        val credential = PhoneAuthProvider.getCredential(verificationId!!, otpCode)
                        auth.signInWithCredential(credential)
                            .addOnCompleteListener { task ->
                                isLoading = false
                                if (task.isSuccessful) {
                                    Toast.makeText(context, "Phone sign-in successful!", Toast.LENGTH_SHORT).show()
                                    navController.navigate(Screen.Home.route) {
                                        popUpTo(navController.graph.startDestinationId) { inclusive = true }
                                    }
                                } else {
                                    errorMessage = task.exception?.localizedMessage ?: "Invalid code."
                                }
                            }
                    } else {
                        errorMessage = "Please enter the 6-digit code."
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple),
                enabled = !isLoading
            ) {
                Text("Verify Code", style = MaterialTheme.typography.titleLarge)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Resend Code Link (Optional, but good UX)
            Text(
                text = "Resend Code",
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold, color = TextBlack),
                modifier = Modifier.clickable {
                    // Reset UI and resend code
                    otpCode = ""
                    isCodeSent = false
                    // Trigger the send code logic again (e.g., by making the button visible again)
                    // Or you can directly call PhoneAuthProvider.verifyPhoneNumber again here
                    // For simplicity, let's just make the phone input UI reappear
                }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Back to Sign In",
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold, color = TextBlack),
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