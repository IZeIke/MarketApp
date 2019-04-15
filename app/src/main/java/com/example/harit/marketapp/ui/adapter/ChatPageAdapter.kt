package com.example.harit.marketapp.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.harit.marketapp.R
import com.example.harit.marketapp.ui.model.ChatModel

class ChatPageAdapter(val context: Context,val myUid: String, private val chatList: MutableList<ChatModel>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


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

    fun addList(list: MutableList<ChatModel>) {
        for (item in list) {
            chatList.add(item)
        }
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ChatViewHolder) {

        }
    }

    class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        /* var cardView = itemView.cardView
        var imageView = itemView.imageView
        var tvName = itemView.textName
        var tvPrice = itemView.textPrice
        var nameTag = itemView.name
        var photosetTypeTag = itemView.photosetType
        var typeTag = itemView.type
        var setTag = itemView.set*/
    }

}