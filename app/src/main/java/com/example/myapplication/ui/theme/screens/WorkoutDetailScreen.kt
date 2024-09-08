package com.example.myapplication.ui.theme.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
data class Exercises(
    val name: String = "",
    val sets: Int = 0,
    val reps: Int = 0
)

@Composable
fun WorkoutDetailScreen(navController: NavHostController, program: String) {
    val context = LocalContext.current
    var workoutData by remember { mutableStateOf<List<Exercises>>(emptyList()) }
    val coroutineScope = rememberCoroutineScope()

    // Function to fetch workout data for the selected program
    fun fetchWorkoutData() {
        coroutineScope.launch {
            val database = FirebaseDatabase.getInstance().getReference("WorkoutPrograms")
            try {
                val dataSnapshot = database.child(program).get().await()
                val workoutList = mutableListOf<Exercises>()
                for (snapshot in dataSnapshot.children) {
                    val exercise = snapshot.getValue(Exercises::class.java)
                    exercise?.let { workoutList.add(it) }
                }
                workoutData = workoutList
            } catch (e: Exception) {
                Log.e("Firebase", "Error fetching workout data", e)
                Toast.makeText(context, "Failed to load workout data", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Fetch workout data when the screen is loaded
    LaunchedEffect(Unit) {
        fetchWorkoutData()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = "Workout Program: $program", style = MaterialTheme.typography.h5)

        if (workoutData.isNotEmpty()) {
            workoutData.forEach { exercise ->
                Text(
                    text = "${exercise.name} - Sets: ${exercise.sets}, Reps: ${exercise.reps}",
                    style = MaterialTheme.typography.body1,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
        } else {
            Text(text = "Loading exercises...", style = MaterialTheme.typography.body2)
        }
    }
}
