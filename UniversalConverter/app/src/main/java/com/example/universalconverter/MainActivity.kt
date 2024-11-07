package com.example.universalconverter

import android.os.Bundle
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.view.View


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Set edge-to-edge layout
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Referensi elemen UI
        val spinnerCategory: Spinner = findViewById(R.id.spinnerCategory)
        val spinnerFromUnit: Spinner = findViewById(R.id.spinnerFromUnit)
        val spinnerToUnit: Spinner = findViewById(R.id.spinnerToUnit)
        val editTextValue: EditText = findViewById(R.id.editTextValue)
        val buttonConvert: Button = findViewById(R.id.buttonConvert)
        val textViewResult: TextView = findViewById(R.id.textViewResult)

        // Daftar kategori
        val categories = arrayOf("Length", "Mass", "Time")
        val adapterCategory = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, categories)
        spinnerCategory.adapter = adapterCategory

        // Peta faktor konversi
        val lengthConversionFactors = mapOf("km" to 1000.0, "m" to 1.0, "cm" to 0.01, "mm" to 0.001)
        val massConversionFactors = mapOf("kg" to 1.0, "hg" to 0.1, "dag" to 0.01, "g" to 0.001, "dg" to 0.0001, "cg" to 0.00001, "mg" to 0.000001)
        val timeConversionFactors = mapOf("h" to 3600.0, "min" to 60.0, "s" to 1.0)

        // Variabel untuk menyimpan faktor konversi yang dipilih
        var selectedFactors = lengthConversionFactors

        // Atur spinner satuan sesuai kategori yang dipilih
        spinnerCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                selectedFactors = when (categories[position]) {
                    "Length" -> lengthConversionFactors
                    "Mass" -> massConversionFactors
                    "Time" -> timeConversionFactors
                    else -> lengthConversionFactors
                }
                updateUnitSpinners(selectedFactors, spinnerFromUnit, spinnerToUnit)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        // Tombol konversi
        buttonConvert.setOnClickListener {
            val value = editTextValue.text.toString().toDoubleOrNull()
            val fromUnit = spinnerFromUnit.selectedItem.toString()
            val toUnit = spinnerToUnit.selectedItem.toString()

            if (value != null) {
                val result = convert(value, fromUnit, toUnit, selectedFactors)
                textViewResult.text = "Result: $result"
            } else {
                textViewResult.text = "Please enter a valid number"
            }
        }
    }

    // Fungsi untuk memperbarui unit spinner sesuai faktor yang dipilih
    private fun updateUnitSpinners(factors: Map<String, Double>, fromSpinner: Spinner, toSpinner: Spinner) {
        val units = factors.keys.toTypedArray()
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, units)
        fromSpinner.adapter = adapter
        toSpinner.adapter = adapter
    }

    // Fungsi konversi nilai berdasarkan satuan asal dan tujuan
    private fun convert(value: Double, fromUnit: String, toUnit: String, factors: Map<String, Double>): Double {
        val fromFactor = factors[fromUnit] ?: error("Invalid from unit")
        val toFactor = factors[toUnit] ?: error("Invalid to unit")
        return value * (fromFactor / toFactor)
    }
}
