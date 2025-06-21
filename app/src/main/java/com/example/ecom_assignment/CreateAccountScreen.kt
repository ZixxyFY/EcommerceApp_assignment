package com.example.ecom_assignment

import android.util.Log // For Log.e calls
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController // For @Preview

// Import your reusable composables from the correct package (ui.theme)
import com.example.ecom_assignment.ui.theme.InputField
import com.example.ecom_assignment.ui.theme.PrimaryPurple
import com.example.ecom_assignment.ui.theme.TextBlack

// Firebase imports
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore // <<< Firestore Import
import com.google.firebase.Timestamp // <<< Timestamp Import

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateAccountScreen(navController: NavController) {
    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()
    val firestore = FirebaseFirestore.getInstance() // Get Firestore instance

    var firstName by remember { mutableStateOf("") } // State for First Name
    var lastName by remember { mutableStateOf("") }  // State for Last Name
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

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
        Text(
            "Create Account",
            style = MaterialTheme.typography.headlineLarge
        )

        Spacer(modifier = Modifier.height(32.dp))

        // UI elements for First Name and Last Name (ADDED)
        InputField(
            value = firstName,
            onValueChange = { firstName = it },
            placeholder = "First Name",
            isPassword = false
        )
        Spacer(modifier = Modifier.height(16.dp))

        InputField(
            value = lastName,
            onValueChange = { lastName = it },
            placeholder = "Last Name",
            isPassword = false
        )
        Spacer(modifier = Modifier.height(16.dp))
        // End of new UI elements

        InputField(
            value = email,
            onValueChange = { email = it },
            placeholder = "Email Address",
            isPassword = false
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
                // Input validation (now includes first and last name)
                if (firstName.isNotBlank() && lastName.isNotBlank() && email.isNotBlank() && password.isNotBlank()) {
                    isLoading = true
                    // Step 1: Create user with Firebase Authentication
                    auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener { authTask ->
                            if (authTask.isSuccessful) {
                                val firebaseUser = authTask.result?.user
                                firebaseUser?.let { user ->
                                    // Step 2: If Auth user created, store additional data in Firestore
                                    val userData = hashMapOf(
                                        "userId" to user.uid, // Store Firebase Auth UID as unique identifier
                                        "firstName" to firstName,
                                        "lastName" to lastName,
                                        "email" to email,
                                        "createdAt" to Timestamp.now() // Add creation timestamp
                                    )

                                    firestore.collection("users") // Your Firestore collection name
                                        .document(user.uid) // Use the user's UID as the document ID
                                        .set(userData) // Store the HashMap
                                        .addOnSuccessListener {
                                            isLoading = false
                                            Toast.makeText(context, "Account created successfully!", Toast.LENGTH_SHORT).show()
                                            // Navigate to home screen after both Auth and Firestore are done
                                            navController.navigate(Screen.Home.route) {
                                                popUpTo(navController.graph.startDestinationId) { inclusive = true }
                                            }
                                        }
                                        .addOnFailureListener { e ->
                                            isLoading = false
                                            errorMessage = "Failed to save user data: ${e.localizedMessage}"
                                            Log.e("CreateAccount", "Firestore write failed for user ${user.uid}: ${e.message}", e)
                                            // IMPORTANT: If Firestore write fails, delete the FirebaseAuth user
                                            // to prevent orphaned accounts. This makes the create account process atomic.
                                            user.delete().addOnCompleteListener { deleteTask ->
                                                if (deleteTask.isSuccessful) {
                                                    Log.i("CreateAccount", "Firestore write failed, FirebaseAuth user ${user.uid} deleted to rollback.")
                                                } else {
                                                    Log.e("CreateAccount", "Firestore write failed, failed to delete FirebaseAuth user ${user.uid}.", deleteTask.exception)
                                                }
                                            }
                                        }
                                } ?: run {
                                    isLoading = false
                                    errorMessage = "Account created, but user object was null. Data not saved."
                                }
                            } else {
                                isLoading = false
                                errorMessage = authTask.exception?.localizedMessage ?: "Account creation failed."
                            }
                        }
                } else {
                    errorMessage = "Please fill all fields (First Name, Last Name, Email, Password)."
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple),
            shape = RoundedCornerShape(28.dp),
            enabled = !isLoading // Disable button while loading
        ) {
            Text("Create Account", style = MaterialTheme.typography.titleLarge)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Already have an Account?",
                style = MaterialTheme.typography.bodySmall
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "Sign in",
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

@Preview(showBackground = true)
@Composable
fun PreviewCreateAccountScreen() {
    CreateAccountScreen(navController = rememberNavController())
}