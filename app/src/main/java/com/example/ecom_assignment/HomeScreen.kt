package com.example.ecom_assignment.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.ecom_assignment.R
import com.example.ecom_assignment.ui.theme.PrimaryPurple
import androidx.compose.ui.graphics.Color
import com.example.ecom_assignment.Screen



@Composable
fun HomeScreen(navController: NavController) {
    Column(modifier = Modifier.fillMaxSize().padding(24.dp), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
        Image(painter = painterResource(R.drawable.email_sent), contentDescription = null, modifier = Modifier.size(120.dp))
        Spacer(modifier = Modifier.height(32.dp))
        Text("Welcome!", fontSize = 24.sp, fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = { navController.navigate(Screen.Login.route) { popUpTo(0) } },
            shape = RoundedCornerShape(28.dp), colors = ButtonDefaults.buttonColors(PrimaryPurple)) {
            Text("Logout", color = Color.White)
        }
    }
}
