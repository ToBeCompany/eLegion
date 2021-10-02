package com.castprogramms.elegion.tools

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.castprogramms.elegion.R
import com.castprogramms.elegion.data.TelegramAddress
import com.castprogramms.elegion.databinding.ItemChatBinding

class ChatsAdapter(
    private val value: List<TelegramAddress>?, private val openUri: (String) -> Unit
) : RecyclerView.Adapter<ChatsAdapter.ChatsViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatsAdapter.ChatsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_chat, parent, false)
        return ChatsViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatsAdapter.ChatsViewHolder, position: Int) {
        holder.bind(value!![position])
    }

    override fun getItemCount(): Int = value!!.size

    inner class ChatsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemChatBinding.bind(view)

        fun bind(address: TelegramAddress){
            Log.e("AAA", address.uri)
            binding.chatTitle.text = address.title.toString()
            binding.chatTitle.setOnClickListener{
                openUri(address.uri)
            }
        }
    }

}