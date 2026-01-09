package com.example.myapplication.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.myapplication.ui.screens.*

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Register : Screen("register")
    object Home : Screen("home")
    object PetRegistration : Screen("pet_registration")
    object MyPets : Screen("my_pets")
    object Consultations : Screen("consultations")
    object ConsultationDetail : Screen("consultation_detail/{consultationId}") {
        fun createRoute(consultationId: String) = "consultation_detail/$consultationId"
    }
    object NearbyVets : Screen("nearby_vets")
    object Prescriptions : Screen("prescriptions")
}

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ) {
        composable(Screen.Login.route) {
            LoginScreen(navController = navController)
        }
        composable(Screen.Register.route) {
            RegisterScreen(navController = navController)
        }
        composable(Screen.Home.route) {
            HomeScreen(navController = navController)
        }
        composable(Screen.PetRegistration.route) {
            PetRegistrationScreen(navController = navController)
        }
        composable(Screen.MyPets.route) {
            MyPetsScreen(navController = navController)
        }
        composable(Screen.Consultations.route) {
            ConsultationsScreen(navController = navController)
        }
        composable(Screen.NearbyVets.route) {
            NearbyVetsScreen(navController = navController)
        }
        composable(Screen.Prescriptions.route) {
            PrescriptionsScreen(navController = navController)
        }
    }
}
