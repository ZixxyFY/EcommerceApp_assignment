package com.example.ecom_assignment.ui.theme

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp // Import for sp unit if you want to use it for custom font sizes


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    isPassword: Boolean = false
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(placeholder) },
        singleLine = true,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
        keyboardOptions = KeyboardOptions.Default
    )
}

@Composable
fun SocialLoginButton(
    iconRes: Int,
    text: String,
    onClick: () -> Unit,
    enabled: Boolean = true,
    // Add iconColor parameter for more control
    iconColor: Color = Color.Unspecified // Default to Unspecified to use icon's original color if not explicitly set
) {
    OutlinedButton(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            // Adjusted height for better visual balance
            .height(48.dp), // Slightly smaller than 56.dp
        shape = RoundedCornerShape(28.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = Color.White,
            contentColor = Color.Black // This is for the text color and button border color
        ),
        enabled = enabled
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = null,
                modifier = Modifier.size(20.dp), // Slightly reduced icon size
                tint = iconColor // Apply the iconColor here
            )
            Spacer(modifier = Modifier.width(12.dp)) // Slightly reduced spacing
            Text(
                text = text,
                style = MaterialTheme.typography.bodyLarge.copy(fontSize = 16.sp) // Adjusted text style and size
            )
        }
    }
}