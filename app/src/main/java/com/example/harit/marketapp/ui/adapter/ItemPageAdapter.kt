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
import com.example.harit.marketapp.ui.model.FeedModel
import kotlinx.android.synthetic.main.item_feed_page.view.*
import kotlinx.android.synthetic.main.item_view_item_page.view.*

class ItemPageAdapter(val context: Context, val feedItem: FeedModel, private var feedListSame: MutableList<FeedModel>?, private var feedListOther: MutableList<FeedModel>?,private val width: Int) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


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
                holder.profile_image.setImageUrl(feedItem.user?.imageUrl)
                holder.userHolder.setOnClickListener {
                    context.startActivity(Intent(context,UserActivity::class.java).putExtra("user",feedItem.user))
                }
            }
        }else if(position == 1){
            if(holder is RecyclerViewHolder){
                feedListSame?.let {
                    holder.topTextHolder.visibility = View.VISIBLE
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
                    holder.topTextHolder.visibility = View.VISIBLE
                    holder.topText.text = "โฟโต้เซ็ทอื่นๆของ ${feedListOther!![0].filter!!["name"]}"
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