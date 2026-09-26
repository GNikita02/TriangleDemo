package com.example.triangledemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.triangledemo.ui.theme.TriangleDemoTheme
import kotlin.math.sqrt

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TriangleDemoTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TriangleDemoTheme {
        Greeting("Android")
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

