package com.example.homeworktracker

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.json.JSONArray
import org.json.JSONObject
import java.util.Calendar

class HomeworkActivity : AppCompatActivity() {

    private val homeworkList = mutableListOf<HomeworkItem>()
    private lateinit var adapter: HomeworkAdapter
    private var selectedDate = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_homework)

        val editSubject = findViewById<EditText>(R.id.editSubject)
        val editTask = findViewById<EditText>(R.id.editTask)
        val btnSave = findViewById<Button>(R.id.btnSave)
        val btnBack = findViewById<Button>(R.id.btnBack)
        val btnPickDate = findViewById<Button>(R.id.btnPickDate)
        val txtSelectedDate = findViewById<TextView>(R.id.txtSelectedDate)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerHomework)

        val sharedPreferences = getSharedPreferences("HomeworkData", Context.MODE_PRIVATE)

        loadHomework(sharedPreferences.getString("homework_json", null))

        adapter = HomeworkAdapter(homeworkList) { position ->
            homeworkList.removeAt(position)
            adapter.notifyItemRemoved(position)
            saveHomework(sharedPreferences)
            Toast.makeText(this, "Homework Deleted", Toast.LENGTH_SHORT).show()
        }
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        btnPickDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            DatePickerDialog(
                this,
                { _, year, month, day ->
                    selectedDate = "$day/${month + 1}/$year"
                    txtSelectedDate.text = "Due: $selectedDate"
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        btnBack.setOnClickListener { finish() }

        btnSave.setOnClickListener {
            val subject = editSubject.text.toString()
            val task = editTask.text.toString()

            if (subject.isEmpty() || task.isEmpty()) {
                Toast.makeText(this, "Fill in all fields", Toast.LENGTH_SHORT).show()
            } else {
                homeworkList.add(HomeworkItem(subject, task, selectedDate))
                adapter.notifyItemInserted(homeworkList.size - 1)
                saveHomework(sharedPreferences)
                Toast.makeText(this, "Homework Saved", Toast.LENGTH_SHORT).show()
                editSubject.text.clear()
                editTask.text.clear()
                txtSelectedDate.text = "No date selected"
                selectedDate = ""
            }
        }
    }

    private fun loadHomework(json: String?) {
        if (json == null) return
        val array = JSONArray(json)
        for (i in 0 until array.length()) {
            val obj = array.getJSONObject(i)
            homeworkList.add(
                HomeworkItem(
                    obj.getString("subject"),
                    obj.getString("task"),
                    obj.optString("dueDate", "")
                )
            )
        }
    }

    private fun saveHomework(sharedPreferences: android.content.SharedPreferences) {
        val array = JSONArray()
        for (item in homeworkList) {
            val obj = JSONObject()
            obj.put("subject", item.subject)
            obj.put("task", item.task)
            obj.put("dueDate", item.dueDate)
            array.put(obj)
        }
        sharedPreferences.edit().putString("homework_json", array.toString()).apply()
    }
}