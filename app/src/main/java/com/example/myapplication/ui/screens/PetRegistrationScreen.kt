package com.example.myapplication.ui.screens

import android.app.Application
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.myapplication.data.Pet
import com.example.myapplication.viewmodel.AuthViewModel
import com.example.myapplication.viewmodel.PetViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PetRegistrationScreen(navController: NavController, authViewModel: AuthViewModel) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val petViewModel: PetViewModel = viewModel(
        factory = androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.getInstance(
            context.applicationContext as Application
        )
    )
    val currentUser by authViewModel.currentUser.collectAsState()
    val accessToken by authViewModel.accessToken.collectAsState()
    val petError by petViewModel.error.collectAsState()

    // Set access token when screen loads
    LaunchedEffect(accessToken) {
        accessToken?.let { petViewModel.setAccessToken(it) }
    }

    var petName by remember { mutableStateOf("") }
    var breed by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var medicalHistory by remember { mutableStateOf("") }
    var species by remember { mutableStateOf("Dog") }
    var isSaving by remember { mutableStateOf(false) }
    var showError by remember { mutableStateOf(false) }
    var showSuccess by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Register Pet") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = petName,
                onValueChange = { petName = it },
                label = { Text("Pet Name") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = breed,
                onValueChange = { breed = it },
                label = { Text("Breed") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedTextField(
                    value = age,
                    onValueChange = { age = it },
                    label = { Text("Age (years)") },
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )

                var expanded by remember { mutableStateOf(false) }
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded },
                    modifier = Modifier.weight(1f)
                ) {
                    OutlinedTextField(
                        value = species,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Species") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        modifier = Modifier.menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        listOf("Dog", "Cat", "Bird", "Rabbit", "Other").forEach { item ->
                            DropdownMenuItem(
                                text = { Text(item) },
                                onClick = {
                                    species = item
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            }

            OutlinedTextField(
                value = medicalHistory,
                onValueChange = { medicalHistory = it },
                label = { Text("Medical History (optional)") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                maxLines = 5
            )

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    if (petName.isNotBlank() && breed.isNotBlank() && age.isNotBlank() && currentUser != null) {
                        isSaving = true
                        showError = false
                        showSuccess = false
                        val pet = Pet(
                            name = petName,
                            breed = breed,
                            age = age.toIntOrNull() ?: 0,
                            medicalHistory = medicalHistory,
                            species = species
                        )
                        scope.launch {
                            val success = petViewModel.addPet(currentUser!!, pet)
                            isSaving = false
                            if (success) {
                                showSuccess = true
                                // Wait a bit then navigate back
                                kotlinx.coroutines.delay(1000)
                                navController.popBackStack()
                            } else {
                                showError = true
                            }
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isSaving && petName.isNotBlank() && breed.isNotBlank() && age.isNotBlank() && currentUser != null
            ) {
                if (isSaving) {
                    CircularProgressIndicator(modifier = Modifier.size(20.dp))
                } else {
                    Text("Save Pet")
                }
            }
        }

        // Show error dialog
        if (showError && petError != null) {
            AlertDialog(
                onDismissRequest = { 
                    showError = false
                    petViewModel.clearError()
                },
                title = { Text("Error") },
                text = { Text(petError ?: "Failed to register pet") },
                confirmButton = {
                    TextButton(onClick = { 
                        showError = false
                        petViewModel.clearError()
                    }) {
                        Text("OK")
                    }
                }
            )
        }

        // Show success dialog
        if (showSuccess) {
            AlertDialog(
                onDismissRequest = { showSuccess = false },
                title = { Text("Success") },
                text = { Text("Pet registered successfully!") },
                confirmButton = {
                    TextButton(onClick = { showSuccess = false }) {
                        Text("OK")
                    }
                }
            )
        }
    }
}
