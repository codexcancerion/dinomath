package com.example.dinasourcalculator.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.dinasourcalculator.ui.theme.Blue40

@Composable
fun HistoryScreen(navController: NavController, historyList: List<String>) {
    // Background color for the entire screen
    Surface(
        color = Color(0xFF080C18)
    ) {
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
                modifier = Modifier
                    .padding(bottom = 10.dp)
            )

            historyList.forEach { historyItem ->
                Text(
                    text = historyItem,
                    fontSize = 25.sp,
                    color = Color.White,
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth())
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Back button to Calculator screen
            Button(
                onClick = { navController.popBackStack() },
                modifier = Modifier
                    .padding(top = 16.dp)
                    .fillMaxWidth(),
                shape = CircleShape,
                colors = ButtonColors(Color.White, Blue40, Color.White, Blue40)
            ) {
                Text(text = "Back to Calculator")
            }
        }
    }
}
