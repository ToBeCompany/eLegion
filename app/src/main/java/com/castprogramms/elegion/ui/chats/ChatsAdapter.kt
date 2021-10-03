package com.castprogramms.elegion.ui.chats

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
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_chat, parent, false)
        return ChatsViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatsViewHolder, position: Int) {
        holder.bind(value!![position])
    }

    override fun getItemCount(): Int = value!!.size

    inner class ChatsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemChatBinding.bind(view)

        fun bind(address: TelegramAddress){
            binding.chatTitle.text = address.title.toString()
            binding.root.setOnClickListener{
                openUri(address.uri)
            }
        }
    }
}