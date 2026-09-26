package com.example.triangledemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.triangledemo.ui.theme.TriangleDemoTheme
import kotlin.math.sqrt

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TriangleDemoTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    TriangleScreen(Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TriangleScreen(modifier: Modifier = Modifier) {
    val options = listOf(
        "к" to "Катет",
        "г" to "Гипотенуза",
        "п" to "Площадь"
    )
    var letter by remember { mutableStateOf("к") }
    var open by remember { mutableStateOf(false) }
    var input by remember { mutableStateOf("") }
    var error by remember { mutableStateOf("") }
    var result by remember { mutableStateOf<TriangleElements?>(null) }

    Column(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Прямоугольный равнобедренный треугольник")

        ExposedDropdownMenuBox(expanded = open, onExpandedChange = { open = it }) {
            OutlinedTextField(
                value = options.first { it.first == letter }.second,
                onValueChange = {},
                readOnly = true,
                label = { Text("Что задано") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = open) },
                modifier = Modifier
                    .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                    .fillMaxWidth()
            )
            ExposedDropdownMenu(expanded = open, onDismissRequest = { open = false }) {
                options.forEach { (itemLetter, title) ->
                    DropdownMenuItem(
                        text = { Text(title) },
                        onClick = {
                            letter = itemLetter
                            open = false
                            result = null
                        }
                    )
                }
            }
        }

        OutlinedTextField(
            value = input,
            onValueChange = {
                input = it
                error = ""
                result = null
            },
            label = { Text("Значение") },
            isError = error.isNotEmpty(),
            supportingText = {
                if (error.isNotEmpty()) {
                    Text(error)
                }
            },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Button(onClick = {
            val value = checkValue(input)
            if (value == null) {
                error = "Нужно число больше нуля"
                result = null
            } else {
                error = ""
                result = solve(letter, value)
            }
        }) {
            Text("Рассчитать")
        }

        val ready = result
        if (ready != null) {
            Text("Катет: ${"%.4f".format(ready.leg)}")
            Text("Гипотенуза: ${"%.4f".format(ready.hypotenuse)}")
            Text("Площадь: ${"%.4f".format(ready.area)}")
        }
    }
}

fun checkValue(text: String): Double? {
    val x = text.trim().replace(',', '.').toDoubleOrNull()
    if (x == null || x <= 0.0) {
        return null
    }
    return x
}

data class TriangleElements(val leg: Double, val hypotenuse: Double, val area: Double)

fun fromLeg(leg: Double): TriangleElements {
    val hypotenuse = leg * sqrt(2.0)
    val area = leg * leg / 2.0
    return TriangleElements(leg, hypotenuse, area)
}

fun fromHypotenuse(hypotenuse: Double): TriangleElements {
    val leg = hypotenuse / sqrt(2.0)
    val area = leg * leg / 2.0
    return TriangleElements(leg, hypotenuse, area)
}

fun fromArea(area: Double): TriangleElements {
    val leg = sqrt(2.0 * area)
    val hypotenuse = leg * sqrt(2.0)
    return TriangleElements(leg, hypotenuse, area)
}

fun solve(letter: String, value: Double): TriangleElements? {
    return when (letter) {
        "к" -> fromLeg(value)
        "г" -> fromHypotenuse(value)
        "п" -> fromArea(value)
        else -> null
    }
}

