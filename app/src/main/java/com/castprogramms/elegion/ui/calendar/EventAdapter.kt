package com.castprogramms.elegion.ui.calendar

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.castprogramms.elegion.R

class EventAdapter: RecyclerView.Adapter<EventAdapter.EventViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        return EventViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.item_event, parent, false))
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        holder.bind()
    }

    override fun getItemCount() = 6

    inner class EventViewHolder(view: View): RecyclerView.ViewHolder(view){
        fun bind(){

        }
    }
}