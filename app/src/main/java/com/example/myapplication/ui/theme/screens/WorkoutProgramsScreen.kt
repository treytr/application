package com.example.myapplication.ui.theme.screens

import androidx.compose.foundation.layout.*
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun WorkoutProgramsScreen(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(text = "Choose exercises below", style = MaterialTheme.typography.h5)


        // Muscular Program Button
        Button(
            onClick = {
                // Navigate to WorkoutDetailScreen for Muscular
                navController.navigate("workoutDetail/Muscular")
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Workouts")
        }

    }
}
