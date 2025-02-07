package com.example.dinasourcalculator.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.dinasourcalculator.ui.theme.Blue40
import com.example.dinasourcalculator.ui.theme.Cyan40
import com.example.dinasourcalculator.ui.theme.Cyan80
import java.util.Stack

@Composable
fun CalculatorScreen(navController: NavController, historyList: List<String>, onSaveHistory: (String) -> Unit) {
    var input by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("") }

    Surface(modifier = Modifier.fillMaxSize(), color = Color(0xFF080C18)) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Dino Math \uD83E\uDD96",
                fontSize = 32.sp,
                color = Color.DarkGray,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(bottom = 10.dp)
            )

            Text(
                text = input,
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
                    .padding(horizontal = 20.dp),
                maxLines = 1
            )

            Text(
                text = result,
                fontSize = 60.sp,
                fontWeight = FontWeight.Bold,
                color = Cyan80,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp)
                    .padding(horizontal = 20.dp),
                maxLines = 1
            )

            val buttons = listOf(
                listOf("+ Pos", "- Neg", "C", "÷"),
                listOf("7", "8", "9", "×"),
                listOf("4", "5", "6", "-"),
                listOf("1", "2", "3", "+"),
                listOf(".", "0", "%", "=")
            )

            buttons.forEach { row ->
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                    row.forEach { label ->
                        val buttonColor = when (label) {
                            "C", "=" -> Cyan40
                            else -> Blue40
                        }

                        Button(
                            onClick = {
                                val newInput = handleButtonClick(input, label, onSaveHistory)
                                if (label == "=") {
                                    result = evaluateExpression(input)
                                    onSaveHistory("$input = $result")
                                } else if (label == "C") {
                                    input = ""
                                    result = ""
                                } else {
                                    input = newInput
                                }
                            },
                            modifier = Modifier
                                .padding(6.dp)
                                .size(70.dp)
                                .background(buttonColor, CircleShape),
                            shape = CircleShape,
                            colors = ButtonColors(Blue40, Color.White, Blue40, Color.White)
                        ) {
                            Text(
                                text = label,
                                fontSize = 16.sp,
                                color = Color.White
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { navController.navigate("history") },
                modifier = Modifier
                    .padding(top = 16.dp)
                    .fillMaxWidth(),
                shape = CircleShape,
                colors = ButtonColors(Color.White, Blue40, Color.White, Blue40)
            ) {
                Text(text = "View History", fontSize = 18.sp, color = Color.Black)
            }
        }
    }
}

fun handleButtonClick(currentInput: String, button: String, onSaveHistory: (String) -> Unit): String {
    return when (button) {
        "C" -> "" // Clear input
        "+ Positive" -> currentInput.toggleSign() // Change to positive
        "- Negative" -> currentInput.toggleSign() // Change to negative
        "=" -> currentInput // "=" button doesn't change input
        else -> currentInput + button // Add the button text to current input
    }
}

fun evaluateExpression(expression: String): String {
    return try {
        val cleanExpr = expression.replace("×", "*").replace("÷", "/")
        val result = evaluateMathExpression(cleanExpr)
        result.toString()
    } catch (e: Exception) {
        "Error"
    }
}

fun evaluateMathExpression(expression: String): Double {
    val tokens = mutableListOf<String>()
    var num = StringBuilder()

    for (char in expression) {
        if (char.isDigit() || char == '.') {
            num.append(char)
        } else if (char in listOf('+', '-', '*', '/', '%')) {
            if (num.isNotEmpty()) {
                tokens.add(num.toString())
                num = StringBuilder()
            }
            tokens.add(char.toString())
        }
    }

    if (num.isNotEmpty()) {
        tokens.add(num.toString())
    }

    val operatorPrecedence = listOf('*', '/', '%')
    val resultStack = Stack<Double>()

    var i = 0
    while (i < tokens.size) {
        val token = tokens[i]
        if (token in operatorPrecedence.map { it.toString() }) {
            val left = resultStack.pop()
            val operator = token[0]
            val right = tokens[++i].toDouble()
            when (operator) {
                '*' -> resultStack.push(left * right)
                '/' -> resultStack.push(left / right)
                '%' -> resultStack.push(left % right)
            }
        } else {
            if (i == 0 || tokens[i - 1] in operatorPrecedence.map { it.toString() }) {
                resultStack.push(token.toDouble())
            }
        }
        i++
    }

    val finalResult = resultStack.reduce { acc, number -> acc + number }

    return finalResult
}

fun String.toggleSign(): String {
    return if (this.isNotEmpty() && this[0] == '-') {
        this.substring(1)
    } else {
        "-$this"
    }
}