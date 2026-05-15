package com.example.homeworktracker

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val editName = findViewById<EditText>(R.id.editName)
        val btnSaveName = findViewById<Button>(R.id.btnSaveName)
        val btnBack = findViewById<Button>(R.id.btnBack)
        val txtName = findViewById<TextView>(R.id.txtName)

        val sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)

        val savedName = sharedPreferences.getString("username", "Student")
        txtName.text = "Welcome, $savedName"

        btnBack.setOnClickListener {
            finish()
        }

        btnSaveName.setOnClickListener {
            val name = editName.text.toString()

            if (name.isEmpty()) {
                Toast.makeText(this, "Enter a name", Toast.LENGTH_SHORT).show()
            } else {
                val editor = sharedPreferences.edit()
                editor.putString("username", name)
                editor.apply()

                txtName.text = "Welcome, $name"
                Toast.makeText(this, "Preference Saved", Toast.LENGTH_SHORT).show()
            }
        }
    }
}