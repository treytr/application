package com.example.myapplication.ui.theme.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.*
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await

@Composable
fun PersonalInformationScreen(navController: NavHostController) {
    val auth = FirebaseAuth.getInstance()
    val userId = auth.currentUser?.uid
    val context = LocalContext.current

    var name by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }
    var height by remember { mutableStateOf("") }

    var loading by remember { mutableStateOf(false) }

    // Function to save personal information to Firebase
    fun savePersonalInfo(name: String, age: String, weight: String, height: String, onComplete: () -> Unit) {
        if (userId != null) {
            val database = FirebaseDatabase.getInstance().getReference("Users")
            val userData = mapOf(
                "name" to name,
                "age" to age,
                "weight" to weight,
                "height" to height
            )

            database.child(userId).setValue(userData)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(context, "Information saved successfully", Toast.LENGTH_SHORT).show()
                        onComplete() // Call the onComplete function to trigger navigation
                    } else {
                        Toast.makeText(context, "Failed to save information", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    // Function to retrieve personal information from Firebase
    suspend fun retrievePersonalInfo() {
        if (userId != null) {
            val database = FirebaseDatabase.getInstance().getReference("Users")
            try {
                val dataSnapshot = database.child(userId).get().await()
                name = dataSnapshot.child("name").value?.toString() ?: ""
                age = dataSnapshot.child("age").value?.toString() ?: ""
                weight = dataSnapshot.child("weight").value?.toString() ?: ""
                height = dataSnapshot.child("height").value?.toString() ?: ""
            } catch (e: Exception) {
                Log.e("Firebase", "Error retrieving user data", e)
                Toast.makeText(context, "Failed to load information", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Load personal info when the screen is displayed
    LaunchedEffect(Unit) {
        loading = true
        retrievePersonalInfo()
        loading = false
    }

    if (loading) {
        CircularProgressIndicator()
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(text = "Personal Information", style = MaterialTheme.typography.h5)

            // Name Input Field
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Name") },
                modifier = Modifier.fillMaxWidth()
            )

            // Age Input Field
            OutlinedTextField(
                value = age,
                onValueChange = { age = it },
                label = { Text("Age") },
                modifier = Modifier.fillMaxWidth()
            )

            // Weight Input Field
            OutlinedTextField(
                value = weight,
                onValueChange = { weight = it },
                label = { Text("Weight (kg)") },
                modifier = Modifier.fillMaxWidth()
            )

            // Height Input Field
            OutlinedTextField(
                value = height,
                onValueChange = { height = it },
                label = { Text("Height (cm)") },
                modifier = Modifier.fillMaxWidth()
            )

            // Save Button
            Button(
                onClick = {
                    savePersonalInfo(name, age, weight, height) {
                        // Navigate to WorkoutProgramsScreen after saving the data
                        navController.navigate("workout_programs")
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Save Information")
            }
        }
    }
}
