package com.example.myapplication.navigation



import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.ui.theme.screens.WorkoutProgramsScreen

import com.example.myapplication.navigation.Routes.ROUTE_DETAIL_SCREEN
import com.example.myapplication.ui.theme.screens.SignupScreen


import com.example.myapplication.navigation.Routes.ROUTE_LOGIN
import com.example.myapplication.navigation.Routes.ROUTE_PERSONAL_INFO
import com.example.myapplication.navigation.Routes.ROUTE_PROGRAM

import com.example.myapplication.navigation.Routes.ROUTE_REGISTER
import com.example.myapplication.ui.theme.screens.LoginScreen

import com.example.myapplication.ui.theme.screens.PersonalInformationScreen







@Composable
fun AppNavHost(modifier: Modifier = Modifier, navController: NavHostController = rememberNavController(), startDestination:String= ROUTE_LOGIN) {
    NavHost(
        navController = navController,
        modifier = modifier,
        startDestination = startDestination
    ) {
        composable(ROUTE_LOGIN) {
            LoginScreen(
                navController
            )
        }

        composable(ROUTE_REGISTER) {
            SignupScreen(navController)
        }
        composable(ROUTE_PERSONAL_INFO) {
            PersonalInformationScreen(
                navController
            )
        }
        composable(ROUTE_PROGRAM) {
            WorkoutProgramsScreen(
                navController
            )
        }
        composable(ROUTE_DETAIL_SCREEN) {
            val workoutType = ""
            WorkoutDetailsScreen(
                navController,
                workoutType,

            )
        }

    }


}

fun WorkoutDetailsScreen(navController: NavHostController, workoutType: String) {
    TODO("Not yet implemented")
}




