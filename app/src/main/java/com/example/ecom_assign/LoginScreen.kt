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
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun LoginScreen(navController: NavController) {
    var email by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 24.dp, end = 24.dp, top = 100.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text("Sign in", fontSize = 28.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(12.dp))

        TextField(
            value = email,
            onValueChange = { email = it },
            placeholder = { Text("Email Address", color = Color.Gray.copy(alpha = 0.4f)) },
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF5F5F5), RoundedCornerShape(8.dp)),
            shape = RoundedCornerShape(8.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color(0xFFF5F5F5),
                unfocusedContainerColor = Color(0xFFF5F5F5),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { navController.navigate(Screen.Password.route) }, // FIXED
            modifier = Modifier.fillMaxWidth().height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF9C6BFF)),
            shape = RoundedCornerShape(28.dp)
        ) {
            Text("Continue", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row {
            Text("Don't have an Account?", fontSize = 12.sp)
            Text(
                text = "Create one",
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                color = Color.Black,
                modifier = Modifier.clickable {
                    navController.navigate(Screen.CreateAccount.route) // FIXED
                }
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        SocialButton("Continue with Apple", navController)
        Spacer(modifier = Modifier.height(12.dp))
        SocialButton("Continue with Google", navController)
        Spacer(modifier = Modifier.height(12.dp))
        SocialButton("Continue with Facebook", navController)
    }
}

@Composable
fun SocialButton(text: String, navController: NavController) {
    Button(
        onClick = { navController.navigate(Screen.Password.route) },
        modifier = Modifier.fillMaxWidth().height(56.dp),
        shape = RoundedCornerShape(28.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF5F5F5)),
        elevation = ButtonDefaults.buttonElevation(0.dp)
    ) {
        Text(text = text, color = Color.Black)
    }
}
