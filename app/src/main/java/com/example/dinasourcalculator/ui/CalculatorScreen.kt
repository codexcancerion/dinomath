package com.example.dinasourcalculator.ui

import androidx.compose.foundation.background
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
                modifier = Modifier.padding(bottom = 10.dp)
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
                listOf("", "+/-", "C", "÷"),
                listOf("7", "8", "9", "×"),
                listOf("4", "5", "6", "-"),
                listOf("1", "2", "3", "+"),
                listOf(".", "0", "%", "=")
            )

            buttons.forEach { row ->
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                    row.forEach { label ->
                        if (label.isNotEmpty()) {
                            val buttonColor = when (label) {
                                "C", "=" -> Cyan40
                                else -> Blue40
                            }

                            Button(
                                onClick = {
                                    if (label == "=") {
                                        result = evaluateExpression(input)
                                        onSaveHistory("$input = $result")
                                    } else {
                                        input = handleButtonClick(input, label)
                                        if (label == "C") result = ""
                                    }
                                },
                                modifier = Modifier
                                    .padding(6.dp)
                                    .size(70.dp)
                                    .background(buttonColor, CircleShape),
                                shape = CircleShape,
                                colors = ButtonDefaults.buttonColors(Blue40)
                            ) {
                                Text(text = label, fontSize = 16.sp, color = Color.White)
                            }
                        } else {
                            Spacer(modifier = Modifier.size(70.dp)) // Placeholder for empty button slot
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
                colors = ButtonDefaults.buttonColors(Color.White)
            ) {
                Text(text = "View History", fontSize = 18.sp, color = Color.Black)
            }
        }
    }
}
// Function to handle button clicks
fun handleButtonClick(currentInput: String, button: String): String {
    return when (button) {
        "C" -> "" // Clear input
        "+/-" -> toggleLastNumberSign(currentInput) // Toggle last number's sign
        "=" -> currentInput // "=" does not change input
        "+", "-", "×", "÷", "%" -> handleOperatorInput(currentInput, button) // Handle operators
        else -> currentInput + button // Append other button values
    }
}

// Function to toggle the sign of the last number in the input
fun toggleLastNumberSign(input: String): String {
    val regex = """(-?\d+\.?\d*)$""".toRegex()
    return regex.replace(input) { match ->
        val num = match.value
        if (num.startsWith("-")) num.substring(1) else "-$num"
    }
}

// Function to correctly handle operators (+, -, ×, ÷, %)
fun handleOperatorInput(input: String, operator: String): String {
    if (input.isEmpty() && operator != "-") return input // Don't allow standalone operators except for negative
    if (input.endsWith("+") || input.endsWith("-") || input.endsWith("×") || input.endsWith("÷") || input.endsWith("%")) {
        return input.dropLast(1) + operator // Replace last operator
    }
    return input + operator // Append operator
}

// Function to evaluate expressions
fun evaluateExpression(expression: String): String {
    return try {
        val cleanExpr = expression.replace("×", "*").replace("÷", "/")
        val result = evaluateMathExpression(cleanExpr)
        result.toString()
    } catch (e: Exception) {
        "Error"
    }
}

// Function to evaluate mathematical expressions correctly
fun evaluateMathExpression(expression: String): Double {
    val tokens = mutableListOf<String>()
    var num = StringBuilder()
    var lastWasOperator = true

    for (char in expression) {
        if (char.isDigit() || char == '.') {
            num.append(char)
            lastWasOperator = false
        } else if (char in listOf('+', '-', '*', '/', '%')) {
            if (char == '-' && lastWasOperator) {
                num.append(char) // Handle negative numbers correctly
            } else {
                if (num.isNotEmpty()) {
                    tokens.add(num.toString())
                    num = StringBuilder()
                }
                tokens.add(char.toString())
                lastWasOperator = true
            }
        }
    }
    if (num.isNotEmpty()) {
        tokens.add(num.toString())
    }

    // Evaluate multiplication, division, and modulus first
    val resultStack = Stack<Double>()
    val operatorStack = Stack<String>()
    val precedence = mapOf("*" to 2, "/" to 2, "%" to 2, "+" to 1, "-" to 1)

    for (token in tokens) {
        if (token in precedence.keys) {
            while (!operatorStack.isEmpty() && precedence[operatorStack.peek()]!! >= precedence[token]!!) {
                applyOperation(resultStack, operatorStack.pop())
            }
            operatorStack.push(token)
        } else {
            resultStack.push(token.toDouble())
        }
    }

    while (!operatorStack.isEmpty()) {
        applyOperation(resultStack, operatorStack.pop())
    }

    return resultStack.pop()
}

// Helper function to apply operations
fun applyOperation(stack: Stack<Double>, operator: String) {
    val right = stack.pop()
    val left = stack.pop()
    when (operator) {
        "+" -> stack.push(left + right)
        "-" -> stack.push(left - right)
        "*" -> stack.push(left * right)
        "/" -> stack.push(left / right)
        "%" -> stack.push(left % right)
    }
}