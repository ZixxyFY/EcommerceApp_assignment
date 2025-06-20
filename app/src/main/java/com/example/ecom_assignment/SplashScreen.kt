// app/src/main/java/com/example/ecom_assign/SplashScreen.kt
package com.example.ecom_assignment

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.* // Ensure MaterialTheme is imported
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.dp
import com.example.ecom_assignment.ui.theme.PrimaryPurple // Import custom color

@Composable
fun SplashScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = PrimaryPurple), // Use defined primary color
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Clot",
            style = MaterialTheme.typography.displaySmall.copy(color = Color.White) // Apply defined displaySmall style
        )

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp)
                .width(100.dp)
                .height(4.dp)
                .background(color = Color.White, shape = RoundedCornerShape(50))
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SplashScreenPreview() {
    // Wrap with your theme for accurate preview
    com.example.ecom_assignment.ui.theme.EcommerceApp_assignmentTheme {
        SplashScreen()
    }
}