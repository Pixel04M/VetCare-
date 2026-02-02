package com.example.myapplication.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myapplication.ui.theme.*
import com.example.myapplication.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConsultationDetailScreen(navController: NavController, consultationId: String, authViewModel: AuthViewModel) {
    var messageText by remember { mutableStateOf("") }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(Turquoise80),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Person, null, modifier = Modifier.size(20.dp), tint = Color.White)
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text("Dr. Sarah Wilson", style = MaterialTheme.typography.titleMedium)
                            Text("Online • Veterinary Expert", style = MaterialTheme.typography.bodySmall, color = Turquoise40)
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { /* Toggle Camera */ }) {
                        Icon(Icons.Default.Refresh, "Switch Camera", tint = Turquoise40)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(LightGray)
        ) {
            // 1. VIDEO AREA (Mint/Turquoise split screen)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.4f)
                    .padding(8.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color.Black)
            ) {
                // Main Video (The Vet)
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Face, null, modifier = Modifier.size(80.dp), tint = Turquoise80.copy(alpha = 0.5f))
                    Text(
                        "Dr. Sarah Wilson", 
                        modifier = Modifier.align(Alignment.BottomCenter).padding(16.dp),
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Small Video Overlay (My Pet)
                Box(
                    modifier = Modifier
                        .size(120.dp, 160.dp)
                        .padding(12.dp)
                        .align(Alignment.TopEnd)
                        .clip(RoundedCornerShape(16.dp))
                        .background(DarkGray)
                        .padding(2.dp)
                ) {
                    Box(modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(14.dp)).background(Color.Gray)) {
                         Icon(Icons.Default.Favorite, null, modifier = Modifier.align(Alignment.Center).size(30.dp), tint = Coral80)
                         Text("My Pet", modifier = Modifier.align(Alignment.BottomCenter).padding(4.dp), fontSize = 10.sp, color = Color.White)
                    }
                }

                // Video Controls
                Row(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FloatingActionButton(
                        onClick = { },
                        containerColor = Coral80,
                        contentColor = Color.White,
                        modifier = Modifier.size(40.dp),
                        shape = CircleShape
                    ) {
                        Icon(Icons.Default.Close, "End Call")
                    }
                }
            }

            // 2. CHAT AREA (Messenger Style)
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.6f)
                    .clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)),
                color = Color.White,
                shadowElevation = 8.dp
            ) {
                Column(modifier = Modifier.fillMaxSize()) {
                    // Chat Messages
                    LazyColumn(
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 16.dp),
                        contentPadding = PaddingValues(vertical = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        item {
                            ChatBubble("Hello! How can I help Buddy today?", isFromMe = false)
                        }
                        item {
                            ChatBubble("He's been coughing a bit since morning.", isFromMe = true)
                        }
                        item {
                            ChatBubble("I see. Can you show me his breathing patterns using the camera?", isFromMe = false)
                        }
                    }

                    // Input Field
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .imePadding(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextField(
                            value = messageText,
                            onValueChange = { messageText = it },
                            placeholder = { Text("Describe symptoms...") },
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(24.dp)),
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = LightGray,
                                unfocusedContainerColor = LightGray,
                                disabledContainerColor = LightGray,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                            ),
                            trailingIcon = {
                                IconButton(onClick = { /* Attachment */ }) {
                                    Icon(Icons.Default.Add, null, tint = Turquoise40)
                                }
                            }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        FloatingActionButton(
                            onClick = { 
                                if (messageText.isNotBlank()) messageText = ""
                            },
                            containerColor = Turquoise80,
                            contentColor = Color.White,
                            shape = CircleShape,
                            modifier = Modifier.size(48.dp)
                        ) {
                            Icon(Icons.Default.Send, null)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ChatBubble(text: String, isFromMe: Boolean) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = if (isFromMe) Alignment.End else Alignment.Start
    ) {
        Box(
            modifier = Modifier
                .widthIn(max = 280.dp)
                .clip(
                    RoundedCornerShape(
                        topStart = 16.dp,
                        topEnd = 16.dp,
                        bottomStart = if (isFromMe) 16.dp else 0.dp,
                        bottomEnd = if (isFromMe) 0.dp else 16.dp
                    )
                )
                .background(if (isFromMe) Turquoise80 else LightGray)
                .padding(12.dp)
        ) {
            Text(
                text = text,
                color = if (isFromMe) Color.White else DarkGray,
                fontSize = 14.sp
            )
        }
    }
}
