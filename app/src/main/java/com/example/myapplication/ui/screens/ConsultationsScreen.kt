package com.example.myapplication.ui.screens

import android.app.Application
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.myapplication.data.ConsultationType
import com.example.myapplication.viewmodel.AuthViewModel
import com.example.myapplication.viewmodel.ConsultationViewModel
import com.example.myapplication.viewmodel.PetViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConsultationsScreen(navController: NavController) {
    val context = LocalContext.current
    val consultationViewModel: ConsultationViewModel = viewModel(
        factory = androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.getInstance(
            context.applicationContext as Application
        )
    )
    val petViewModel: PetViewModel = viewModel(
        factory = androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.getInstance(
            context.applicationContext as Application
        )
    )
    val authViewModel = remember { AuthViewModel() }
    val currentUser = authViewModel.currentUser.collectAsState()

    LaunchedEffect(currentUser.value) {
        currentUser.value?.let { consultationViewModel.loadConsultations(it) }
    }

    val consultations by consultationViewModel.consultations.collectAsState()
    val pets by petViewModel.pets.collectAsState()
    var showDialog by remember { mutableStateOf(false) }
    var selectedPetId by remember { mutableStateOf<String?>(null) }
    var selectedType by remember { mutableStateOf<ConsultationType?>(null) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(currentUser.value) {
        currentUser.value?.let { petViewModel.loadPets(it) }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Consultations") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, "Back")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    if (pets.isNotEmpty()) {
                        selectedPetId = pets.first().id
                        showDialog = true
                    }
                }
            ) {
                Icon(Icons.Default.Add, "Start Consultation")
            }
        }
    ) { padding ->
        if (consultations.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Icon(
                        Icons.Default.Call,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f)
                    )
                    Text(
                        text = "No consultations yet",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = "Start a chat or video call with a vet",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(consultations) { consultation ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { navController.navigate("consultation_detail/${consultation.id}") },
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                if (consultation.type == ConsultationType.VIDEO_CALL) Icons.Default.Call else Icons.Default.Edit,
                                contentDescription = null,
                                modifier = Modifier.size(48.dp),
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = consultation.vetName,
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Text(
                                    text = consultation.type.name,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                                )
                                Text(
                                    text = "Status: ${consultation.status.name}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Start Consultation") },
            text = {
                Column {
                    Text("Select consultation type:")
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = {
                            selectedType = ConsultationType.CHAT
                            showDialog = false
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.Edit, null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Chat")
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = {
                            selectedType = ConsultationType.VIDEO_CALL
                            showDialog = false
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.Call, null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Video Call")
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    LaunchedEffect(selectedType) {
        selectedType?.let { type ->
            selectedPetId?.let { petId ->
                currentUser.value?.let { userId ->
                    scope.launch {
                        consultationViewModel.startConsultation(userId, petId, type)
                    }
                }
            }
        }
    }
}
