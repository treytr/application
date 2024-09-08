package com.example.myapplication.ui.theme.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SignupScreen(navController: NavHostController) {
    val auth = FirebaseAuth.getInstance()
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }
    var isConfirmPasswordVisible by remember { mutableStateOf(false) }
    val context = LocalContext.current
    var isEmailVerified by remember { mutableStateOf(false) }
    var verificationMessage by remember { mutableStateOf("") }
    var isVerifyingEmail by remember { mutableStateOf(false) }
    var showLoginButton by remember { mutableStateOf(false) }

    // To launch a coroutine when needed
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(text = "Sign Up", style = MaterialTheme.typography.h5)

        // Email Input Field
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )

        // Password Input Field with visibility toggle
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                    Icon(imageVector = if (isPasswordVisible) Icons.Default.MoreVert else Icons.Default.MoreVert, contentDescription = null)
                }
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )

        // Confirm Password Input Field with visibility toggle
        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Confirm Password") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = if (isConfirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { isConfirmPasswordVisible = !isConfirmPasswordVisible }) {
                    Icon(imageVector = if (isConfirmPasswordVisible) Icons.Default.MoreVert else Icons.Default.MoreVert, contentDescription = null)
                }
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )

        // Sign Up Button
        Button(
            onClick = {
                if (password == confirmPassword) {
                    auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                // Send email verification
                                val user = auth.currentUser
                                user?.sendEmailVerification()?.addOnCompleteListener { verificationTask ->
                                    if (verificationTask.isSuccessful) {
                                        isVerifyingEmail = true
                                        verificationMessage = "Verifying email..."
                                        showLoginButton = false

                                        // Launch a coroutine to check email verification
                                        coroutineScope.launch {
                                            val verified = checkEmailVerification(auth)
                                            if (verified) {
                                                verificationMessage = "Verified, proceed to login"
                                                isEmailVerified = true
                                                showLoginButton = true
                                                isVerifyingEmail = false
                                            }
                                        }
                                    } else {
                                        Toast.makeText(context, "Failed to send verification email", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            } else {
                                Toast.makeText(context, "Signup failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                            }
                        }
                } else {
                    Toast.makeText(context, "Passwords do not match", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Sign Up")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Display verification message
        if (verificationMessage.isNotEmpty()) {
            Text(text = verificationMessage, color = MaterialTheme.colors.primary)
        }

        // Show login button after email is verified
        if (showLoginButton) {
            Button(
                onClick = { navController.navigate("login") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Go to Login")
            }
        }

        // Add a Login Button in case user already has an account
        Button(
            onClick = { navController.navigate("login") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Already have an account? Login")
        }
    }
}

// Function to periodically check email verification status
suspend fun checkEmailVerification(auth: FirebaseAuth): Boolean {
    val user = auth.currentUser
    if (user != null) {
        // Delay to give the user time to verify the email
        while (!user.isEmailVerified) {
            delay(3000) // Check every 3 seconds
            user.reload() // Reload user data to get updated verification status
        }
        return true
    }
    return false
}
