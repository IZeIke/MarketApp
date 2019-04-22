package com.example.harit.marketapp.ui.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.harit.marketapp.R
import com.example.harit.marketapp.extention.setImageUrl
import com.example.harit.marketapp.ui.itemPage.ItemPageActivity
import com.example.harit.marketapp.ui.model.*
import com.example.harit.marketapp.ui.searchPage.FeedActivity
import com.robertlevonyan.components.picker.set
import kotlinx.android.synthetic.main.view_item_feed.view.*

class FeedPageAdapter(val context: Context, private val feedList: MutableList<FeedModel>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.view_item_feed,parent,false))
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

            feedList[position].let {feedModel ->
                holder.imageView.set(R.drawable.mockup)
                feedModel.imageUrl?.let {list ->
                    if(list.size > 0)
                        holder.imageView.setImageUrl(list[0])
                }
                holder.tvName.text = feedModel.name
                //if(feedModel.status != "sold") {
                    holder.tvPrice.text = feedModel.price.toString()
                /*}else{
                    holder.tvPrice.text = "ขายแล้ว"
                    holder.dollar.visibility = View.GONE
                }*/
                holder.nameTag.text = feedModel.filter?.get("name")
                holder.nameTag.setOnClickListener {
                    var model = SearchModel(Format.ALL, feedModel.filter?.get("name")!!, Sort.LASTED,0, Type.ALL)
                    context.startActivity(Intent(context, FeedActivity::class.java).putExtra("searchModel",model))
                }
                holder.photosetTypeTag.text = feedModel.filter?.get("photosetType")
                holder.photosetTypeTag.setOnClickListener {
                    var model = SearchModel(feedModel.filter?.get("photosetType")!!, "All", Sort.LASTED,0, Type.ALL)
                    context.startActivity(Intent(context, FeedActivity::class.java).putExtra("searchModel",model))
                }
                holder.typeTag.text = feedModel.filter?.get("type")
                holder.typeTag.setOnClickListener {
                    var model = SearchModel(Format.ALL, "All", Sort.LASTED,0, feedModel.filter?.get("type")!!)
                    context.startActivity(Intent(context, FeedActivity::class.java).putExtra("searchModel",model))
                }
                holder.setTag.text = feedModel.filter?.get("set")
                val strs = feedModel.filter?.get("set")?.split(" ")?.toTypedArray()
                holder.setTag.setOnClickListener {
                    var model = SearchModel(Format.ALL, "All", Sort.LASTED, strs!![1].toInt(), Type.ALL)
                    context.startActivity(Intent(context, FeedActivity::class.java).putExtra("searchModel",model))
                }
            }

            holder.cardView.setOnClickListener {
                val intent = Intent(context,ItemPageActivity::class.java)
                intent.putExtra("item",feedList[position])
                context.startActivity(intent)
            }
        }
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
        var dollar = itemView.dollarSign
    }

}