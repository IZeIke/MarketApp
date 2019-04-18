package com.example.harit.marketapp.ui.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.example.harit.marketapp.R
import com.example.harit.marketapp.extention.setImageUrl
import com.example.harit.marketapp.ui.itemPage.ItemPageActivity
import com.example.harit.marketapp.ui.model.FeedItem
import com.example.harit.marketapp.ui.model.FeedModel
import com.robertlevonyan.components.picker.set
import kotlinx.android.synthetic.main.view_item_sell.view.*


class SellItemPageAdapter(val context: Context, private val feedList: MutableList<FeedModel>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.view_item_sell,parent,false))
    }

    override fun getItemCount(): Int {
        return feedList.size
    }

    fun addList(list : MutableList<FeedModel>){
        for(item in list){
            feedList.add(item)
        }
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is ItemViewHolder){

            feedList[position].let {
                holder.imageView.set(com.example.harit.marketapp.R.drawable.mockup)
                it.imageUrl?.let {list ->
                    if(list.size > 0)
                        holder.imageView.setImageUrl(list[0])
                }
                holder.tvName.text = it.name
                holder.tvPrice.text = "฿"+it.price.toString()
                holder.nameTag.text = it.filter?.get("name")
                holder.photosetTypeTag.text = it.filter?.get("photosetType")
                holder.typeTag.text = it.filter?.get("type")
                holder.setTag.text = it.filter?.get("set")
            }

            holder.option.setOnClickListener {
                showPopup(holder)
            }

            holder.cardView.setOnClickListener {
                val intent = Intent(context, ItemPageActivity::class.java)
                intent.putExtra("item",feedList[position])
                context.startActivity(intent)
            }
        }
    }

    private fun showPopup(holder: ItemViewHolder) {
        val popup = PopupMenu(context, holder.option)
        //inflating menu from xml resource
        popup.inflate(R.menu.menu_item_sell)
        //adding click listener
        popup.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.menu1 -> {

                }
                R.id.menu2 -> {

                }
                R.id.menu3 -> {

                }
            }
            false
        }
        popup.show()
    }

    class ItemViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        var cardView = itemView.cardView
        var imageView = itemView.imageView
        var tvName = itemView.textName
        var tvPrice = itemView.textPrice
        var nameTag = itemView.name
        var photosetTypeTag = itemView.photosetType
        var typeTag = itemView.type
        var setTag = itemView.set
        var option = itemView.textViewOptions
    }

}