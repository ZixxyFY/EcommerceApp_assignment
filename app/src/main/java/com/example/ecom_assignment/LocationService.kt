package com.example.ecom_assignment

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.OnTokenCanceledListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.Timestamp
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import java.util.*

class LocationService(private val context: Context) {
    private val fusedLocationClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
    private val geocoder = Geocoder(context, Locale.getDefault())
    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    fun hasLocationPermission(): Boolean {
        return ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED ||
        ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    suspend fun getCurrentLocation(): Location? {
        if (!hasLocationPermission()) {
            throw SecurityException("Location permission not granted")
        }

        return suspendCancellableCoroutine { continuation ->
            try {
                fusedLocationClient.getCurrentLocation(
                    Priority.PRIORITY_HIGH_ACCURACY,
                    object : CancellationToken() {
                        override fun onCanceledRequested(listener: OnTokenCanceledListener) = CancellationTokenSource().token
                        override fun isCancellationRequested() = false
                    }
                ).addOnSuccessListener { location ->
                    continuation.resume(location)
                }.addOnFailureListener { exception ->
                    continuation.resumeWithException(exception)
                }
            } catch (e: Exception) {
                continuation.resumeWithException(e)
            }
        }
    }

    suspend fun getAddressFromLocation(location: Location): String? {
        return try {
            val addresses: List<Address>? = geocoder.getFromLocation(location.latitude, location.longitude, 1)
            addresses?.firstOrNull()?.let { address ->
                val addressParts = mutableListOf<String>()
                
                address.thoroughfare?.let { addressParts.add(it) }
                address.subLocality?.let { addressParts.add(it) }
                address.locality?.let { addressParts.add(it) }
                address.adminArea?.let { addressParts.add(it) }
                address.countryName?.let { addressParts.add(it) }
                
                if (addressParts.isNotEmpty()) {
                    addressParts.joinToString(", ")
                } else {
                    "Unknown Location"
                }
            } ?: "Unknown Location"
        } catch (e: Exception) {
            "Unknown Location"
        }
    }

    suspend fun getLocationData(): LocationData? {
        return try {
            val location = getCurrentLocation()
            location?.let {
                val address = getAddressFromLocation(it)
                LocationData(
                    latitude = it.latitude,
                    longitude = it.longitude,
                    address = address ?: "Unknown Location"
                )
            }
        } catch (e: Exception) {
            null
        }
    }

    // Save location data to Firebase Firestore
    suspend fun saveLocationToFirebase(locationData: LocationData) {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val locationDocument = hashMapOf(
                "userId" to currentUser.uid,
                "email" to (currentUser.email ?: ""),
                "latitude" to locationData.latitude,
                "longitude" to locationData.longitude,
                "address" to locationData.address,
                "timestamp" to Timestamp.now()
            )

            try {
                firestore.collection("user_locations")
                    .document(currentUser.uid)
                    .set(locationDocument)
                    .await()
            } catch (e: Exception) {
                throw e
            }
        }
    }

    // Get location history for a user
    suspend fun getLocationHistory(): List<LocationData> {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            try {
                val snapshot = firestore.collection("user_locations")
                    .whereEqualTo("userId", currentUser.uid)
                    .orderBy("timestamp", com.google.firebase.firestore.Query.Direction.DESCENDING)
                    .limit(10)
                    .get()
                    .await()

                return snapshot.documents.mapNotNull { document ->
                    val latitude = document.get("latitude") as? Double
                    val longitude = document.get("longitude") as? Double
                    val address = document.get("address") as? String
                    
                    if (latitude != null && longitude != null && address != null) {
                        LocationData(latitude, longitude, address)
                    } else null
                }
            } catch (e: Exception) {
                return emptyList()
            }
        }
        return emptyList()
    }

    // Get all user locations (for admin purposes)
    suspend fun getAllUserLocations(): List<UserLocationData> {
        try {
            val snapshot = firestore.collection("user_locations")
                .orderBy("timestamp", com.google.firebase.firestore.Query.Direction.DESCENDING)
                .get()
                .await()

            return snapshot.documents.mapNotNull { document ->
                val userId = document.get("userId") as? String
                val email = document.get("email") as? String
                val latitude = document.get("latitude") as? Double
                val longitude = document.get("longitude") as? Double
                val address = document.get("address") as? String
                val timestamp = document.get("timestamp") as? Timestamp
                
                if (userId != null && email != null && latitude != null && longitude != null && address != null) {
                    UserLocationData(
                        userId = userId,
                        email = email,
                        latitude = latitude,
                        longitude = longitude,
                        address = address,
                        timestamp = timestamp
                    )
                } else null
            }
        } catch (e: Exception) {
            return emptyList()
        }
    }
}

data class LocationData(
    val latitude: Double,
    val longitude: Double,
    val address: String
)

data class UserLocationData(
    val userId: String,
    val email: String,
    val latitude: Double,
    val longitude: Double,
    val address: String,
    val timestamp: com.google.firebase.Timestamp?
) 