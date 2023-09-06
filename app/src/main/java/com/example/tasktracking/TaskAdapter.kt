package com.example.tasktracking

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlin.time.DurationUnit
import kotlin.time.toKotlinDuration

class TaskAdapter(private val mList: List<Task>) : RecyclerView.Adapter<TaskAdapter.ViewHolder>() {
    val REP_TASK = 1
    val DUR_TASK = 2

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the duration_task_card_view.xml view
        // that is used to hold list item
        var holder: ViewHolder

        holder = if (viewType == REP_TASK) {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.repetition_task_card_view, parent, false)
            RepetitionViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.duration_task_card_view, parent, false)
            DurationViewHolder(view)
        }

        return holder
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val taskItem = mList[position]

        if (getItemViewType(position) == REP_TASK) {
            val repTaskItem = taskItem as RepetitionTask
            // sets the image to the imageview from our itemHolder class
            val repHolder = holder as RepetitionViewHolder
            // sets the text to the textview from our itemHolder class
            repHolder.nameView.text = repTaskItem.name
            repHolder.completedView.text = "Completed: " + repTaskItem.repCompleted
            repHolder.goalView.text = "of: " + repTaskItem.repGoal
        } else {
            val durTaskItem = taskItem as DurationTask

            val durHolder = holder as DurationViewHolder
            durHolder.nameView.text = durTaskItem.name
            durHolder.completedView.text = "Completed: " + durTaskItem.timeCompleted.toString()
            durHolder.goalView.text = "of: " + durTaskItem.timeGoal.toString()
        }

    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return mList.size
    }

    override fun getItemViewType(position: Int): Int {
        val taskItem = mList[position]

        return if (taskItem is RepetitionTask) {
            REP_TASK
        } else {
            DUR_TASK
        }
    }

    // Holds the views for adding it to image and text
    open class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    }

    class DurationViewHolder(itemView: View) : ViewHolder(itemView) {
        val nameView: TextView = itemView.findViewById(R.id.duration_task_textView_name)
        val completedView: TextView = itemView.findViewById(R.id.duration_task_textView_completed)
        val goalView: TextView = itemView.findViewById(R.id.duration_task_textView_goal)
    }

    class RepetitionViewHolder(itemView: View) : ViewHolder(itemView) {
        val nameView: TextView = itemView.findViewById(R.id.repetition_task_textView_name)
        val completedView: TextView = itemView.findViewById(R.id.repetition_task_textView_completed)
        val goalView: TextView = itemView.findViewById(R.id.repetition_task_textView_goal)
    }
}