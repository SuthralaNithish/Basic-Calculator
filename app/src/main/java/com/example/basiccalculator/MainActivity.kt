package com.example.basiccalculator

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var tvDisplay: TextView
    private var lastNumeric: Boolean = false
    private var lastOperator: Boolean = false
    private var stateError: Boolean = false
    private var lastDot: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvDisplay = findViewById(R.id.tvDisplay)
    }

    fun onDigit(view: View) {
        if (stateError) {
            tvDisplay.text = (view as Button).text
            stateError = false
        } else {
            tvDisplay.append((view as Button).text)
        }
        lastNumeric = true
        lastOperator = false
    }

    fun onOperator(view: View) {
        if (lastNumeric && !stateError) {
            tvDisplay.append((view as Button).text)
            lastNumeric = false
            lastDot = false
            lastOperator = true
        }
    }

    fun onClear(view: View) {
        tvDisplay.text = ""
        lastNumeric = false
        stateError = false
        lastDot = false
        lastOperator = false
    }

    fun onEqual(view: View) {
        if (lastNumeric && !stateError) {
            val txt = tvDisplay.text.toString()
            try {
                val result = evaluate(txt)
                tvDisplay.text = result.toString()
                lastDot = true
            } catch (ex: Exception) {
                tvDisplay.text = "Error"
                stateError = true
                lastNumeric = false
            }
        }
    }

    private fun evaluate(expression: String): Double {
        val tokens = expression.split("(?<=[-+*/])|(?=[-+*/])".toRegex())
        if (tokens.size < 3) return 0.0

        var result = tokens[0].toDouble()
        var operator = ""

        for (token in tokens) {
            when {
                token.matches(Regex("[0-9]+")) -> {
                    if (operator.isNotEmpty()) {
                        result = calculate(result, token.toDouble(), operator)
                        operator = ""
                    }
                }
                token.matches(Regex("[-+*/]")) -> {
                    operator = token
                }
            }
        }
        return result
    }

    private fun calculate(num1: Double, num2: Double, operator: String): Double {
        return when (operator) {
            "+" -> num1 + num2
            "-" -> num1 - num2
            "*" -> num1 * num2
            "/" -> num1 / num2
            else -> 0.0
        }
    }
}
