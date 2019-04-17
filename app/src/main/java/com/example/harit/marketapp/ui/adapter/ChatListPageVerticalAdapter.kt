package com.example.harit.marketapp.ui.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.harit.marketapp.R
import com.example.harit.marketapp.ui.chatPage.ChatActivity
import com.example.harit.marketapp.ui.model.ChatListModel
import kotlinx.android.synthetic.main.view_item_chat_list.view.*
import java.text.SimpleDateFormat
import java.util.*

class ChatListPageVerticalAdapter(val context: Context, private val chatList: MutableList<ChatListModel>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ChatViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.view_item_chat_list, parent, false))
    }

    override fun getItemCount(): Int {
        return chatList.size
    }

    fun addList(item: ChatListModel) {

        chatList.add(item)

        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ChatViewHolder) {
            holder.message.text = chatList[position].message
            holder.name.text = chatList[position].user?.name
            holder.timeText.text = convertTime(chatList[position].create!!)
            holder.chatHolder.setOnClickListener {
                context.startActivity(Intent(context,ChatActivity::class.java).putExtra("user",chatList[position].user))
            }
        }
    }

    class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var message = itemView.messageText
        var timeText = itemView.timeText
        var name = itemView.nameText
        var profilePic = itemView.profile_image
        var chatHolder = itemView.chatHolder
    }

    private fun convertTime(date: Date): String {
        val format = SimpleDateFormat("dd-MM-yyyy HH:mm:ss")
        return format.format(date)
    }
}