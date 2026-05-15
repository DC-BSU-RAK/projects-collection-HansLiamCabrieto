package com.example.homeworktracker

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

data class HomeworkItem(val subject: String, val task: String, val dueDate: String)

class HomeworkAdapter(
    private val items: MutableList<HomeworkItem>,
    private val onDelete: (Int) -> Unit
) : RecyclerView.Adapter<HomeworkAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtSubject: TextView = view.findViewById(R.id.txtSubject)
        val txtTask: TextView = view.findViewById(R.id.txtTask)
        val txtDueDate: TextView = view.findViewById(R.id.txtDueDate)
        val btnDelete: Button = view.findViewById(R.id.btnDelete)
        val cardLayout: View = view
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_homework, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.txtSubject.text = "Subject: ${item.subject}"
        holder.txtTask.text = "Task: ${item.task}"

        if (item.dueDate.isNotEmpty()) {
            holder.txtDueDate.text = "Due: ${item.dueDate}"

            val sdf = SimpleDateFormat("d/M/yyyy", Locale.getDefault())
            val due = sdf.parse(item.dueDate)
            val today = Date()

            if (due != null) {
                val diffMs = due.time - today.time
                val diffDays = TimeUnit.MILLISECONDS.toDays(diffMs)

                when {
                    diffDays < 0 -> {

                        holder.cardLayout.setBackgroundColor(Color.parseColor("#FFCDD2"))
                        holder.txtDueDate.text = "OVERDUE: ${item.dueDate}"
                    }
                    diffDays <= 2 -> {

                        holder.cardLayout.setBackgroundColor(Color.parseColor("#FFE0B2"))
                        holder.txtDueDate.text = "Due soon: ${item.dueDate}"
                    }
                    else -> {

                        holder.cardLayout.setBackgroundColor(Color.parseColor("#F5F5F5"))
                        holder.txtDueDate.text = "Due: ${item.dueDate}"
                    }
                }
            }
        } else {
            holder.txtDueDate.text = "No due date"
            holder.cardLayout.setBackgroundColor(Color.parseColor("#F5F5F5"))
        }

        holder.btnDelete.setOnClickListener {
            onDelete(holder.adapterPosition)
        }
    }

    override fun getItemCount() = items.size
}