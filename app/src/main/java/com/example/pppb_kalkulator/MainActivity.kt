package com.example.pppb_kalkulator

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.pppb_kalkulator.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var lastOperation: String
    private lateinit var savedSubCalc: String
    private var optDone: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        lastOperation = ""
        savedSubCalc = ""
    }

    // Dipanggil ketika angka ditekan.
    fun inputNumber(view: View) {
        val inputtedValue = (view as Button).text.toString()

        with(binding) {
            if (!((mainCalc.text.isEmpty() || (subCalc.text.isNotEmpty() && optDone)) && (inputtedValue == "00" || inputtedValue == "0"))) {
                if (!optDone) {
                    if (lastOperation.isEmpty()) {
                        mainCalc.text = "${mainCalc.text}$inputtedValue"
                    } else {
                        if (subCalc.text.isEmpty()) {
                            subCalc.text = mainCalc.text
                            mainCalc.text = inputtedValue
                        } else {
                            mainCalc.text = "${mainCalc.text}$inputtedValue"
                        }
                    }
                } else {
                    savedSubCalc = ""
                    subCalc.text = ""
                    mainCalc.text = inputtedValue
                    lastOperation = ""
                    optDone = false
                }
            }
        }
    }

    // Dipanggil ketika operasi matematika ditekan.
    fun inputOperation(view: View) {
        val currentOperation = (view as Button).text.toString()

        with(binding) {
            if (!optDone) {
                if (lastOperation.isEmpty() && currentOperation != "C" && currentOperation != "c") {
                    if (mainCalc.text.isNotEmpty()) {
                        savedSubCalc = mainCalc.text.toString()
                        subCalc.text = "${mainCalc.text} $currentOperation"
                        mainCalc.text = ""
                        lastOperation = currentOperation
                    }
                } else if (currentOperation == "c") {
                    if (mainCalc.text.isNotEmpty()) {
                        clearLastDigit()
                    } else if (mainCalc.text.isEmpty() && subCalc.text.isNotEmpty()) {
                        clearAll()
                    }
                } else {
                    if (currentOperation == "=") {
                        if (mainCalc.text.isNotEmpty() && subCalc.text.isNotEmpty()) {
                            calculateResult()
                        }
                    } else if (mainCalc.text.isEmpty() && currentOperation != "C") {
                        lastOperation = currentOperation
                        subCalc.text = "$savedSubCalc $currentOperation"
                    } else if (currentOperation == "C") {
                        clearAll()
                    }
                }
            } else {
                if (currentOperation != "C" && currentOperation != "c" && currentOperation != "=") {
                    if (mainCalc.text.isNotEmpty()) {
                        savedSubCalc = mainCalc.text.toString()
                        subCalc.text = "${mainCalc.text} $currentOperation"
                        mainCalc.text = ""
                    } else {
                        savedSubCalc = ""
                        subCalc.text = ""
                    }

                    if (currentOperation != "=" && optDone) {
                        lastOperation = currentOperation
                    } else {
                        lastOperation = ""
                    }

                    optDone = false
                } else {
                    clearAll()
                }
            }
        }
    }

    // Dipanggil ketika tombol hapus (DEL) ditekan.
    fun inputDelete(view: View) {
        with(binding) {
            if (mainCalc.text.isNotEmpty()) {
                val currentText = mainCalc.text.toString()
                mainCalc.text = currentText.substring(0, currentText.length - 1)
            }
        }
    }

    // Menghapus semua hasil dan operasi yang ada.
    private fun clearAll() {
        with(binding) {
            savedSubCalc = ""
            subCalc.text = ""
            mainCalc.text = ""
            lastOperation = ""
        }
    }

    // Menghapus digit terakhir dari angka yang ditampilkan di layar.
    private fun clearLastDigit() {
        with(binding) {
            if (mainCalc.text.length > 1) {
                val temp = StringBuilder(mainCalc.text.toString())
                temp.deleteCharAt(temp.length - 1)
                mainCalc.text = temp.toString()
            } else if (subCalc.text.isNotEmpty()) {
                mainCalc.text = ""
            } else {
                clearAll()
            }
        }
    }

    // Menghitung hasil dari operasi matematika yang dimasukkan.
    private fun calculateResult() {
        with(binding) {
            try {
                when (lastOperation) {
                    "+" -> mainCalc.text = (mainCalc.text.toString().toBigDecimal() + savedSubCalc.toBigDecimal()).toString()
                    "-" -> mainCalc.text = (savedSubCalc.toBigDecimal() - mainCalc.text.toString().toBigDecimal()).toString()
                    "X" -> mainCalc.text = (mainCalc.text.toString().toBigDecimal() * savedSubCalc.toBigDecimal()).toString()
                    "/" -> {
                        if (!mainCalc.text.toString().contains('.')) mainCalc.text = "${mainCalc.text}.0"
                        if (!savedSubCalc.contains('.')) savedSubCalc = "${savedSubCalc}.0"

                        mainCalc.text = (savedSubCalc.toDouble() / mainCalc.text.toString().toDouble()).toString()
                    }
                    "%" -> mainCalc.text = (savedSubCalc.toBigDecimal() % mainCalc.text.toString().toBigDecimal()).toString()
                }
            } catch (e: NumberFormatException) {
                mainCalc.text = "Angka terlalu besar"
            }

            subCalc.text = ""
            lastOperation = ""
            optDone = true
        }
    }

    // Menambahkan desimal pada angka yang dimasukkan.
    fun makeDecimal(view: View) {
        with(binding) {
            mainCalc.text = "${mainCalc.text}."
        }
    }
}
