package com.example.ecom_assignment

import android.Manifest
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.ecom_assignment.ui.theme.PrimaryPurple
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationServicesScreen(navController: NavController) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val locationService = remember { LocationService(context) }
    
    var locationData by remember { mutableStateOf<LocationData?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    var hasPermission by remember { mutableStateOf(locationService.hasLocationPermission()) }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val locationGranted = permissions.entries.all { it.value }
        hasPermission = locationGranted
        if (locationGranted) {
            scope.launch {
                getLocationData(locationService) { data ->
                    locationData = data
                }
            }
        } else {
            Toast.makeText(context, "Location permission is required", Toast.LENGTH_LONG).show()
        }
    }

    LaunchedEffect(Unit) {
        if (hasPermission) {
            getLocationData(locationService) { data ->
                locationData = data
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Location Services", fontWeight = FontWeight.SemiBold) },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = Color.Black
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Location Icon
            Card(
                modifier = Modifier
                    .size(120.dp)
                    .padding(bottom = 24.dp),
                shape = RoundedCornerShape(60.dp),
                colors = CardDefaults.cardColors(containerColor = PrimaryPurple.copy(alpha = 0.1f))
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = "Location",
                        modifier = Modifier.size(48.dp),
                        tint = PrimaryPurple
                    )
                }
            }

            Text(
                text = "Your Location",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = "Get your current location and address",
                fontSize = 16.sp,
                color = Color.Gray,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            if (!hasPermission) {
                // Permission Request Card
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Location Permission Required",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        Text(
                            text = "This app needs location permission to show your current location and provide location-based services.",
                            fontSize = 14.sp,
                            color = Color.Gray,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                        Button(
                            onClick = {
                                permissionLauncher.launch(
                                    arrayOf(
                                        Manifest.permission.ACCESS_FINE_LOCATION,
                                        Manifest.permission.ACCESS_COARSE_LOCATION
                                    )
                                )
                            },
                            shape = RoundedCornerShape(28.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple)
                        ) {
                            Text("Grant Permission", color = Color.White)
                        }
                    }
                }
            } else {
                // Location Data Card
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Current Location",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            IconButton(
                                onClick = {
                                    scope.launch {
                                        getLocationData(locationService) { data ->
                                            locationData = data
                                        }
                                    }
                                },
                                enabled = !isLoading
                            ) {
                                Icon(
                                    Icons.Default.Refresh,
                                    contentDescription = "Refresh",
                                    tint = if (isLoading) Color.Gray else PrimaryPurple
                                )
                            }
                        }

                        if (isLoading) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 32.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(color = PrimaryPurple)
                            }
                        } else if (locationData != null) {
                            Spacer(modifier = Modifier.height(16.dp))
                            
                            // Address
                            LocationInfoItem(
                                title = "Address",
                                value = locationData!!.address
                            )
                            
                            Spacer(modifier = Modifier.height(12.dp))
                            
                            // Coordinates
                            LocationInfoItem(
                                title = "Latitude",
                                value = String.format("%.6f", locationData!!.latitude)
                            )
                            
                            Spacer(modifier = Modifier.height(8.dp))
                            
                            LocationInfoItem(
                                title = "Longitude",
                                value = String.format("%.6f", locationData!!.longitude)
                            )
                        } else {
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "Unable to get location. Please try again.",
                                color = Color.Gray,
                                fontSize = 14.sp
                            )
                        }
                    }
                }

                // Action Buttons
                Button(
                    onClick = {
                        scope.launch {
                            getLocationData(locationService) { data ->
                                locationData = data
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(28.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple),
                    enabled = !isLoading
                ) {
                    Text("Refresh Location", color = Color.White, fontSize = 16.sp)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Navigation to Home
            OutlinedButton(
                onClick = { navController.navigate(Screen.Home.route) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = Color.White,
                    contentColor = PrimaryPurple
                )
            ) {
                Text("Back to Home", fontSize = 16.sp)
            }
        }
    }
}

@Composable
private fun LocationInfoItem(title: String, value: String) {
    Column {
        Text(
            text = title,
            fontSize = 12.sp,
            color = Color.Gray,
            fontWeight = FontWeight.Medium
        )
        Text(
            text = value,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.Black
        )
    }
}

private suspend fun getLocationData(
    locationService: LocationService,
    onResult: (LocationData?) -> Unit
) {
    try {
        val data = locationService.getLocationData()
        onResult(data)
    } catch (e: Exception) {
        onResult(null)
    }
} 