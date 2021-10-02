package com.castprogramms.elegion.ui.checklist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.castprogramms.elegion.R
import com.castprogramms.elegion.data.CheckItem
import com.castprogramms.elegion.databinding.ItemTaskBinding

class TaskListAdapter(
    private val list: List<CheckItem>,
    private val completeListener: (value : Boolean, tack : CheckItem) -> Unit
) : RecyclerView.Adapter<TaskListAdapter.TaskViewHolder>() {

    inner class TaskViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemTaskBinding.bind(view)
        fun onBind(item: CheckItem) {
            binding.isComplete.isChecked = item.complete
            binding.taskText.text = item.title
            binding.isComplete.setOnCheckedChangeListener { checkBox, isChecked ->
                completeListener(isChecked, item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        return TaskViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_task, parent, false)
        )
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.onBind(list[position])
    }

    override fun getItemCount(): Int = list.size
}