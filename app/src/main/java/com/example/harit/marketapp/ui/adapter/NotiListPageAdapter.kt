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
import com.example.harit.marketapp.ui.model.NotiModel
import kotlinx.android.synthetic.main.view_item_noti_list.view.*
import java.text.SimpleDateFormat
import java.util.*

class NotiListPageAdapter (val context: Context, private val notiList: MutableList<NotiModel>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ChatViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.view_item_noti_list, parent, false))
    }

    override fun getItemCount(): Int {
        return notiList.size
    }

    fun addList(item: NotiModel) {

        notiList.add(item)

        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ChatViewHolder) {
            holder.message.text = notiList[position].message
            holder.name.text = notiList[position].user?.name
            holder.timeText.text = convertTime(notiList[position].create!!)
            notiList[position].user?.imageUrl?.let {
                holder.profilePic.setImageUrl(it)
            }
            holder.chatHolder.setOnClickListener {
                context.startActivity(Intent(context, ChatActivity::class.java).putExtra("user",notiList[position].user))
            }
        }
    }

    class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var message = itemView.notiText
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