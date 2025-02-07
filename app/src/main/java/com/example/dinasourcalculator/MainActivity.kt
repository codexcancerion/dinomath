package com.example.dinasourcalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.composable
import com.example.dinasourcalculator.ui.CalculatorScreen
import com.example.dinasourcalculator.ui.HistoryScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppNavigation()
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val historyList = remember { mutableStateListOf<String>() }

    NavHost(navController = navController, startDestination = "calculator") {
        composable("calculator") {
            CalculatorScreen(
                navController = navController,
                historyList = historyList,
                onSaveHistory = { historyList.add(it) }
            )
        }
        composable("history") {
            HistoryScreen(navController = navController, historyList = historyList)
        }
    }
}
