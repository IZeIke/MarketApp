package com.example.harit.marketapp.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.harit.marketapp.R
import com.example.harit.marketapp.ui.model.Chat
import kotlinx.android.synthetic.main.view_item_chat_2.view.*
import java.text.SimpleDateFormat
import java.util.*


class ChatPageAdapter(val context: Context,val myUid: String, private val chatList: MutableList<Chat>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if(viewType == 0) {
            ChatViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.view_item_chat_1, parent, false))
        }
        else{
            ChatViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.view_item_chat_2, parent, false))
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if(myUid == chatList[position].senderId)
            0
        else
            1
    }

    override fun getItemCount(): Int {
        return chatList.size
    }

    fun addList(item: Chat) {

        chatList.add(item)

        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ChatViewHolder) {
            holder.message.text = chatList[position].message
            holder.timeText.text = convertTime(chatList[position].create!!)
        }
    }

    class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var message = itemView.textChat
        var timeText = itemView.timeText
    }

    private fun convertTime(time: Long): String {
        val date = Date(time)
        val format = SimpleDateFormat("dd-MM-yyyy HH:mm:ss")
        return format.format(date)
    }


}