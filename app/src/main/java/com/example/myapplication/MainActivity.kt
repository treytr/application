package com.example.myapplication





//noinspection UsingMaterialAndMaterial3Libraries
//noinspection UsingMaterialAndMaterial3Libraries


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.MaterialTheme
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.ui.theme.screens.WorkoutProgramsScreen

import com.example.myapplication.ui.theme.screens.WorkoutDetailScreen
import com.example.myapplication.ui.theme.screens.LoginScreen
import com.example.myapplication.ui.theme.screens.PersonalInformationScreen
import com.example.myapplication.ui.theme.screens.SignupScreen


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

                // Set up the main navigation of the app
                MainNavigation()
            }
        }

}

@Composable
fun MainNavigation() {
    val navController = rememberNavController()
    Surface(color = MaterialTheme.colors.background) {
        NavHost(navController = navController, startDestination = "sign_up") {
            composable("sign_up") {
                SignupScreen(navController = navController)
            }
            composable("login") {
                LoginScreen(navController = navController)
            }

            composable("personal_info") {
                PersonalInformationScreen(navController = navController)
            }
            composable("workout_programs") {
                WorkoutProgramsScreen(navController = navController)

            }

            composable("workoutPrograms") { WorkoutProgramsScreen(navController) }
            composable("workoutDetail/{program}") { backStackEntry ->
                val program = backStackEntry.arguments?.getString("program")
                program?.let { WorkoutDetailScreen(navController, it) }
            }


        }
    }
}