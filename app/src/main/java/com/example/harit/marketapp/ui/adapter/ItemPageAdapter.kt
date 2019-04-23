package com.example.harit.marketapp.ui.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.asksira.bsimagepicker.GridItemSpacingDecoration
import com.example.harit.marketapp.R
import com.example.harit.marketapp.extention.setImageUrl
import com.example.harit.marketapp.ui.UserPage.UserActivity
import com.example.harit.marketapp.ui.model.*
import com.example.harit.marketapp.ui.searchPage.FeedActivity
import kotlinx.android.synthetic.main.item_feed_page.view.*
import kotlinx.android.synthetic.main.item_view_item_page.view.*

class ItemPageAdapter(val context: Context, var feedItem: FeedModel, private var feedListSame: MutableList<FeedModel>?, private var feedListOther: MutableList<FeedModel>?,private val width: Int) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == 0) {
            FirstViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_view_item_page, parent, false))
        } else {
            RecyclerViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_feed_page, parent, false))
        }
    }

    override fun getItemViewType(position: Int): Int {
        if(position == 0){
            return 0
        }else{
            return 1
        }
    }

    override fun getItemCount(): Int {
        return 3
    }

    fun addFeedListSame(list : MutableList<FeedModel>){
        feedListSame = list
        notifyDataSetChanged()
    }

    fun addFeedListOther(list : MutableList<FeedModel>){
        feedListOther = list
        notifyDataSetChanged()
    }

    fun addFeedItem(item: FeedModel){
        feedItem = item
        notifyDataSetChanged()
    }

    /*fun addList(item: Chat) {

        chatList.add(item)

        notifyDataSetChanged()
    }*/

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if(position == 0){
            if(holder is FirstViewHolder){
                holder.sellerName.text = feedItem.user?.name
                holder.itemImage.setImageUrl(feedItem.imageUrl!![0])
                holder.itemPrice.text = "฿"+feedItem.price.toString()
                holder.itemName.text = feedItem.name
                holder.itemDes.text = feedItem.description
                holder.payment.text = feedItem.payment
                holder.shipping.text = feedItem.shipping

                holder.nameTag.text = feedItem.filter?.get("name")
                holder.nameTag.setOnClickListener {
                    var model = SearchModel(Format.ALL, feedItem.filter?.get("name")!!, Sort.LASTED,0, Type.ALL)
                    context.startActivity(Intent(context, FeedActivity::class.java).putExtra("searchModel",model))
                }
                holder.photosetTypeTag.text = feedItem.filter?.get("photosetType")
                holder.photosetTypeTag.setOnClickListener {
                    var model = SearchModel(feedItem.filter?.get("photosetType")!!, "All", Sort.LASTED,0, Type.ALL)
                    context.startActivity(Intent(context, FeedActivity::class.java).putExtra("searchModel",model))
                }
                if(feedItem.filter?.get("type") == ""){
                    holder.typeTag.visibility = View.GONE
                }else {
                    holder.typeTag.text = feedItem.filter?.get("type")
                }
                holder.typeTag.setOnClickListener {
                    var model = SearchModel(Format.ALL, "All", Sort.LASTED,0, feedItem.filter?.get("type")!!)
                    context.startActivity(Intent(context, FeedActivity::class.java).putExtra("searchModel",model))
                }
                holder.setTag.text = feedItem.filter?.get("set")
                val strs = feedItem.filter?.get("set")?.split(" ")?.toTypedArray()
                holder.setTag.setOnClickListener {
                    var model = SearchModel(Format.ALL, "All", Sort.LASTED, strs!![1].toInt(), Type.ALL)
                    context.startActivity(Intent(context, FeedActivity::class.java).putExtra("searchModel",model))
                }

                feedItem.user?.imageUrl?.let {
                    holder.profile_image.setImageUrl(it)
                }
                holder.userHolder.setOnClickListener {
                    context.startActivity(Intent(context,UserActivity::class.java).putExtra("user",feedItem.user))
                }
            }
        }else if(position == 1){
            if(holder is RecyclerViewHolder){
                feedListSame?.let {
                    if(feedListSame?.size == 0){
                        holder.topTextHolder.visibility = View.GONE
                    }else {
                        holder.topTextHolder.visibility = View.VISIBLE
                    }
                    holder.allBtn.setOnClickListener {
                        context.startActivity(Intent(context,UserActivity::class.java).putExtra("user",feedItem.user))
                    }
                    holder.feedRecyclerView.let { recyclerView ->
                        recyclerView.visibility = View.VISIBLE
                        recyclerView.layoutManager = GridLayoutManager(context, 1,GridLayoutManager.HORIZONTAL,false)
                        if (recyclerView.itemDecorationCount == 0) {
                            recyclerView.addItemDecoration(GridItemSpacingDecoration(feedListSame!!.size, 20, true))
                        }
                        recyclerView.adapter = FeedPageHorizontalAdapter(context, feedListSame!!,width)
                    }
                }
            }
        }else if(position == 2){
            if(holder is RecyclerViewHolder){
                feedListOther?.let {
                    if(feedListOther?.size == 0){
                        holder.topTextHolder.visibility = View.GONE
                    }else {
                        holder.topTextHolder.visibility = View.VISIBLE
                    }
                    holder.allBtn.setOnClickListener {
                        var model = SearchModel(Format.ALL, feedListOther!![0].filter!!["name"]!!,Sort.LASTED,0,Type.ALL)
                        context.startActivity(Intent(context, FeedActivity::class.java).putExtra("searchModel",model))
                    }
                    holder.topText.text = "โฟโต้เซ็ทอื่นๆของ ${feedItem.filter!!["name"]}"
                    holder.feedRecyclerView.let { recyclerView ->
                        recyclerView.visibility = View.VISIBLE
                        recyclerView.layoutManager = GridLayoutManager(context,1 ,GridLayoutManager.HORIZONTAL,false)
                        if (recyclerView.itemDecorationCount == 0) {
                            recyclerView.addItemDecoration(GridItemSpacingDecoration(feedListOther!!.size, 20, true))
                        }
                        recyclerView.adapter = FeedPageHorizontalAdapter(context, feedListOther!!,width)
                    }
                }
            }
        }

    }

    class FirstViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var itemName = itemView.itemName
        var itemImage = itemView.itemImage
        var itemPrice = itemView.itemPrice
        var sellerName = itemView.sellerName
        var itemDes = itemView.itemDes
        var userHolder = itemView.userHolder
        var profile_image = itemView.profile_image
        var payment = itemView.payment
        var shipping = itemView.shipping
        var nameTag = itemView.name
        var photosetTypeTag = itemView.photosetType
        var typeTag = itemView.type
        var setTag = itemView.set
    }



    class RecyclerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var feedRecyclerView = itemView.feedRecyclerView
        var topTextHolder = itemView.topBarHolder
        var topText = itemView.topBarText
        var allBtn = itemView.allText
    }

   /* private fun convertTime(time: Long): String {
        val date = Date(time)
        val format = SimpleDateFormat("dd-MM-yyyy HH:mm:ss")
        return format.format(date)
    }*/
}