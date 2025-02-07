package com.example.dinasourcalculator.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.dinasourcalculator.ui.theme.Blue40

@Composable
fun HistoryScreen(
    navController: NavController,
    initialHistoryList: List<String>,
    onDeleteItem: (String) -> Unit,
    onClearHistory: () -> Unit
) {
    var historyList by remember { mutableStateOf(initialHistoryList) }

    Surface(color = Color(0xFF080C18)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
                .padding(top = 30.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Calculation History",
                fontSize = 32.sp,
                color = Color.Gray,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 10.dp)
            )

            if (historyList.isEmpty()) {
                Text(
                    text = "No history available",
                    fontSize = 20.sp,
                    color = Color.White,
                    modifier = Modifier.padding(16.dp)
                )
            } else {
                Column(modifier = Modifier.fillMaxWidth()) {
                    historyList.forEach { historyItem ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = historyItem,
                                fontSize = 20.sp,
                                color = Color.White,
                                modifier = Modifier.weight(1f) // Allow text to take available space
                            )

                            Button(
                                onClick = {
                                    historyList = historyList - historyItem
                                    onDeleteItem(historyItem) // Remove from actual history
                                },
                                shape = CircleShape,
                                colors = ButtonDefaults.buttonColors(Color.Red),
                                modifier = Modifier.padding(start = 8.dp)
                            ) {
                                Text(text = "X", color = Color.White, fontSize = 14.sp)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                Button(
                    onClick = {
                        historyList = emptyList()
                        onClearHistory()
                    },
                    shape = CircleShape,
                    colors = ButtonDefaults.buttonColors(Color.Red)
                ) {
                    Text(text = "Clear All", color = Color.White, fontSize = 18.sp)
                }

                Button(
                    onClick = { navController.popBackStack() },
                    shape = CircleShape,
                    colors = ButtonDefaults.buttonColors(Color.White)
                ) {
                    Text(text = "Back to Calculator", color = Color.Black, fontSize = 18.sp)
                }
            }
        }
    }
}
