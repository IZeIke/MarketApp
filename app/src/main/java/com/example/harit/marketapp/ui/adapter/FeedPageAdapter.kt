package com.example.harit.marketapp.ui.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.harit.marketapp.R
import com.example.harit.marketapp.ui.itemPage.ItemPageActivity
import kotlinx.android.synthetic.main.view_item_feed.view.*

class FeedPageAdapter(val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.view_item_feed,parent,false))
    }

    override fun getItemCount(): Int {
        return 20
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is ItemViewHolder){
            holder.cardView.setOnClickListener {
                val intent = Intent(context,ItemPageActivity::class.java)
                context.startActivity(intent)
            }
        }
    }

    class ItemViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        var cardView = itemView.cardView
        var imageView = itemView.imageView
        var tvName = itemView.textName
        var tvPrice = itemView.textPrice
    }

}