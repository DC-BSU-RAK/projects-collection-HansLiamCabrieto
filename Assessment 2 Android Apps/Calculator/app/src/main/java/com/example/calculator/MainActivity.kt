package com.example.calculator

import android.graphics.Color
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val infoBtn = findViewById<TextView>(R.id.infoBtn)

        infoBtn.setOnClickListener {
            val dialog = android.app.AlertDialog.Builder(this)
                .setTitle("⚡ How to Use")
                .setMessage(
                    "Select 2 or 3 chakra natures to discover your Kekkei Genkai or Kekkei Tota.\n\n" +
                            "🔥 Fire  💨 Wind  ⚡ Lightning  🌍 Earth  💧 Water\n\n" +
                            "2 elements → Kekkei Genkai\n" +
                            "3 elements → Kekkei Tota\n\n" +
                            "Not all combinations are valid — only true shinobi can unlock rare releases!"
                )
                .setPositiveButton("Got it!") { d, _ -> d.dismiss() }
                .create()

            dialog.show()
        }
        val element1 = findViewById<Spinner>(R.id.element1)
        val element2 = findViewById<Spinner>(R.id.element2)
        val element3 = findViewById<Spinner>(R.id.element3)
        val resultText = findViewById<TextView>(R.id.resultText)
        val calculateBtn = findViewById<Button>(R.id.calculateBtn)

        val elements = arrayOf("None", "Fire", "Wind", "Lightning", "Earth", "Water")

        val adapter = ArrayAdapter(
            this,
            R.layout.spinner_item,
            elements
        ).also {
            it.setDropDownViewResource(R.layout.spinner_item)
        }  // ← removed the extra ) that was here

        element1.adapter = adapter
        element2.adapter = adapter
        element3.adapter = adapter

        calculateBtn.setOnClickListener {
            val e1 = element1.selectedItem.toString()
            val e2 = element2.selectedItem.toString()
            val e3Raw = element3.selectedItem.toString()
            val e3 = if (e3Raw == "None") null else e3Raw
            val result = getCombination(e1, e2, e3)
            resultText.text = result

            val color = when {
                result.contains("Fire") ||
                        result.contains("Scorch") ||
                        result.contains("Lava") -> "#ef4444"
                result.contains("Water") ||
                        result.contains("Ice") -> "#3b82f6"
                result.contains("Lightning") ||
                        result.contains("Storm") -> "#facc15"
                result.contains("Earth") ||
                        result.contains("Wood") -> "#22c55e"
                result.contains("No Kekkei") -> "#9ca3af"
                else -> "#ffffff"
            }

            resultText.setTextColor(Color.parseColor(color))
        }
    }

    private fun getCombination(e1: String, e2: String, e3: String?): String {
        val list = listOfNotNull(e1, e2, e3).distinct()

        if (list.size == 2) {
            val set = list.toSet()
            return when (set) {
                setOf("Fire", "Wind") -> "Scorch Release"
                setOf("Fire", "Lightning") -> "Explosion Release"
                setOf("Wind", "Lightning") -> "Swift Release"
                setOf("Wind", "Earth") -> "Magnet Release"
                setOf("Lightning", "Earth") -> "Steel Release"
                setOf("Lightning", "Water") -> "Storm Release"
                setOf("Earth", "Water") -> "Wood Release"
                setOf("Fire", "Earth") -> "Lava Release"
                setOf("Fire", "Water") -> "Boil Release"
                setOf("Wind", "Water") -> "Ice Release"
                else -> "No Kekkei Genkai"
            }
        }

        if (list.size == 3) {
            val set = list.toSet()
            return when (set) {
                setOf("Fire", "Lightning", "Wind") -> "Plasma Release"
                setOf("Earth", "Fire", "Wind") -> "Dust Release"
                setOf("Fire", "Water", "Wind") -> "Typhoon Release"
                setOf("Earth", "Fire", "Lightning") -> "Mercury Release"
                setOf("Fire", "Lightning", "Water") -> "Purification Release"
                setOf("Earth", "Fire", "Water") -> "Volcano Release"
                setOf("Earth", "Lightning", "Wind") -> "Gravity Release"
                setOf("Lightning", "Water", "Wind") -> "Blizzard Release"
                setOf("Earth", "Water", "Wind") -> "Seed Release"
                setOf("Earth", "Lightning", "Water") -> "Crystal Release"
                else -> "No Kekkei Tota"
            }
        }

        return "Invalid combination"
    }
}