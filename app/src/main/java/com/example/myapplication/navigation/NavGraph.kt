package com.example.myapplication.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.myapplication.ui.screens.*
import com.example.myapplication.viewmodel.AuthViewModel

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Register : Screen("register")
    object Home : Screen("home")
    object VetHome : Screen("vet_home")
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
    // Create a single instance of AuthViewModel to be shared
    val authViewModel: AuthViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ) {
        composable(Screen.Login.route) {
            LoginScreen(navController = navController, authViewModel = authViewModel)
        }
        composable(Screen.Register.route) {
            RegisterScreen(navController = navController, authViewModel = authViewModel)
        }
        composable(Screen.Home.route) {
            HomeScreen(navController = navController, authViewModel = authViewModel)
        }
        composable(Screen.VetHome.route) {
            HomeScreen(navController = navController, authViewModel = authViewModel)
        }
        composable(Screen.PetRegistration.route) {
            PetRegistrationScreen(navController = navController, authViewModel = authViewModel)
        }
        composable(Screen.MyPets.route) {
            MyPetsScreen(navController = navController, authViewModel = authViewModel)
        }
        composable(Screen.Consultations.route) {
            ConsultationsScreen(navController = navController, authViewModel = authViewModel)
        }
        composable(Screen.ConsultationDetail.route) { backStackEntry ->
            val consultationId = backStackEntry.arguments?.getString("consultationId") ?: ""
            ConsultationDetailScreen(navController = navController, consultationId = consultationId, authViewModel = authViewModel)
        }
        composable(Screen.NearbyVets.route) {
            NearbyVetsScreen(navController = navController)
        }
        composable(Screen.Prescriptions.route) {
            PrescriptionsScreen(navController = navController)
        }
    }
}
