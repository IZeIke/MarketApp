package com.example.harit.marketapp.ui.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.example.harit.marketapp.R
import com.example.harit.marketapp.extention.setImageUrl
import com.example.harit.marketapp.ui.itemPage.ItemPageActivity
import com.example.harit.marketapp.ui.model.FeedModel
import com.robertlevonyan.components.picker.set
import kotlinx.android.synthetic.main.view_item_sell.view.*


class SellItemPageAdapter(val context: Context, private val feedList: MutableList<FeedModel>,private val listener : SellItemPageAdapterInterface) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    interface SellItemPageAdapterInterface{
        fun updateSoldItem(id : String)
        fun deleteItem(id : String)
        fun startEditActivity(feedModel: FeedModel)
    }

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
                if(it.filter?.get("type") == ""){
                    holder.typeTag.visibility = View.GONE
                }else {
                    holder.typeTag.text = it.filter?.get("type")
                }
                holder.setTag.text = it.filter?.get("set")

            }

            holder.option.setOnClickListener {
                showPopup(holder,position)
            }

            holder.cardView.setOnClickListener {
                val intent = Intent(context, ItemPageActivity::class.java)
                intent.putExtra("item",feedList[position])
                context.startActivity(intent)
            }
        }
    }

    private fun showPopup(holder: ItemViewHolder,position: Int) {
        val popup = PopupMenu(context, holder.option)
        //inflating menu from xml resource
        popup.inflate(R.menu.menu_item_sell)
        //adding click listener
        popup.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.menu1 -> {
                    listener.startEditActivity(feedList[position])
                }
                R.id.menu2 -> {
                    listener.updateSoldItem(feedList[position].id!!)
                }
                R.id.menu3 -> {
                    listener.deleteItem(feedList[position].id!!)
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