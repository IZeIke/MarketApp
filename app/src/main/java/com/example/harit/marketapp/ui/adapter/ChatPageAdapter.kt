package com.example.harit.marketapp.ui.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.harit.marketapp.R
import com.example.harit.marketapp.extention.setImageUrl
import com.example.harit.marketapp.ui.chatPage.ChatActivity
import com.example.harit.marketapp.ui.itemPage.ItemPageActivity
import com.example.harit.marketapp.ui.model.Chat
import com.example.harit.marketapp.ui.model.FeedModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.view_buy_chat_1.view.*
import kotlinx.android.synthetic.main.view_item_chat_2.view.*
import java.text.SimpleDateFormat
import java.util.*


class ChatPageAdapter(val context: Context,val myUid: String, private val chatList: MutableList<Chat>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    interface ChatPageAdapterListener{
        fun onStartItemActivity(chat : Chat)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if(viewType == 0) {
            ChatViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.view_item_chat_1, parent, false))
        }
        else if(viewType == 1){
            ChatViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.view_item_chat_2, parent, false))
        }
        else if(viewType == 2){
            ChatBuyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.view_buy_chat_1, parent, false))
        }
        else{
            ChatBuyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.view_buy_chat_2, parent, false))
        }
    }

    override fun getItemViewType(position: Int): Int {
        if(myUid == chatList[position].senderId) {
            if (chatList[position].messageType != "TEXT") {
                return 2
            }else {
                return 0
            }
        }
        else {
            if (chatList[position].messageType != "TEXT") {
                return 3
            }else {
                return 1
            }
        }
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
        }else
        if (holder is ChatBuyViewHolder) {
            val name_buyText = chatList[position].message?.split("_")?.toTypedArray()
            holder.itemname.text = name_buyText!![0]
            holder.buyText.text = name_buyText!![1]
            holder.imageItem.setImageUrl(chatList[position].mediaUrl)
            holder.timeText.text = convertTime(chatList[position].create!!)
            holder.chatHolder.setOnClickListener {
                (context as ChatActivity).onStartItemActivity(chatList[position])
            }
        }
    }

    class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var message = itemView.textChat
        var timeText = itemView.timeText
    }

    class ChatBuyViewHolder(itemView : View) :  RecyclerView.ViewHolder(itemView) {
        var itemname = itemView.name
        var timeText = itemView.time
        var imageItem = itemView.itemImage
        var buyText = itemView.buyText
        var chatHolder = itemView.holder
    }

    private fun convertTime(time: Long): String {
        val date = Date(time)
        val format = SimpleDateFormat("dd-MM-yyyy HH:mm:ss")
        return format.format(date)
    }


}