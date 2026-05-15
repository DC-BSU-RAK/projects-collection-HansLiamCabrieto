package com.example.homeworktracker

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONArray
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnHomework = findViewById<Button>(R.id.btnHomework)
        val btnSettings = findViewById<Button>(R.id.btnSettings)

        btnHomework.setOnClickListener {
            startActivity(Intent(this, HomeworkActivity::class.java))
        }

        btnSettings.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()

        val sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val savedName = sharedPreferences.getString("username", "Student")
        findViewById<TextView>(R.id.txtWelcome).text = "Welcome, $savedName!"

        loadUrgentHomework()
    }

    private fun loadUrgentHomework() {
        val container = findViewById<LinearLayout>(R.id.urgentContainer)
        container.removeAllViews()

        val sharedPreferences = getSharedPreferences("HomeworkData", Context.MODE_PRIVATE)
        val json = sharedPreferences.getString("homework_json", null) ?: return

        val sdf = SimpleDateFormat("d/M/yyyy", Locale.getDefault())
        val today = Date()
        val array = JSONArray(json)

        var hasUrgent = false

        for (i in 0 until array.length()) {
            val obj = array.getJSONObject(i)
            val subject = obj.getString("subject")
            val task = obj.getString("task")
            val dueDate = obj.optString("dueDate", "")

            if (dueDate.isEmpty()) continue

            val due = sdf.parse(dueDate) ?: continue
            val diffDays = TimeUnit.MILLISECONDS.toDays(due.time - today.time)

            if (diffDays >= 3) continue

            hasUrgent = true

            val card = LinearLayout(this).apply {
                orientation = LinearLayout.VERTICAL
                setPadding(24, 16, 24, 16)
                setBackgroundColor(
                    if (diffDays < 0) Color.parseColor("#FFCDD2")
                    else Color.parseColor("#FFE0B2")
                )
                val params = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                params.setMargins(0, 0, 0, 16)
                layoutParams = params
            }

            val txtSubject = TextView(this).apply {
                text = "$subject"
                textSize = 15f
                setTextColor(Color.BLACK)
                setTypeface(null, android.graphics.Typeface.BOLD)
            }

            val txtTask = TextView(this).apply {
                text = "Task: $task"
                textSize = 13f
                setTextColor(Color.DKGRAY)
            }

            val txtDate = TextView(this).apply {
                text = when {
                    diffDays < 0 -> "OVERDUE: $dueDate"
                    diffDays <= 2 -> "Due soon: $dueDate"
                    else -> "Due: $dueDate"
                }
                textSize = 13f
                setTextColor(Color.parseColor("#B71C1C"))
            }

            card.addView(txtSubject)
            card.addView(txtTask)
            card.addView(txtDate)
            container.addView(card)
        }

        findViewById<TextView>(R.id.txtUrgentHeader).visibility =
            if (hasUrgent) TextView.VISIBLE else TextView.GONE
    }
}