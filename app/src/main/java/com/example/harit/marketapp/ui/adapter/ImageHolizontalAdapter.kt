package com.example.harit.marketapp.ui.adapter

import android.content.Context
import android.net.Uri
import android.util.EventLog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.bumptech.glide.Glide
import com.example.harit.marketapp.R
import com.example.harit.marketapp.extention.setImageUri
import kotlinx.android.synthetic.main.view_image_100dp.view.*
import kotlinx.android.synthetic.main.view_image_card.view.*
import org.greenrobot.eventbus.EventBus
import java.io.File

class ImageHolizontalAdapter(val type : Int, private val imageList: List<Uri>) : Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if(viewType == 0)
            ImageViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.view_image_100dp,parent,false))
        else
            ImageCardViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.view_image_card,parent,false))
    }

    override fun getItemViewType(position: Int): Int {
        return if(type == 0){
            0
        }else{
            1
        }
    }

    override fun getItemCount(): Int {
        return if(type == 0)
            imageList.size + 1
        else
            imageList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is ImageViewHolder) {
            when {
                (position == 0) -> {
                    holder.headImage.visibility = View.VISIBLE
                    holder.image.setImageUri(imageList[position])
                }
                (position != itemCount - 1) -> {
                    holder.headImage.visibility = View.GONE
                    holder.image.setImageUri(imageList[position])
                }
                else -> {
                    holder.headImage.visibility = View.GONE
                    holder.image.setImageResource(R.drawable.add_image)
                    holder.image.setOnClickListener {

                    }
                }
            }
        }
        if(holder is ImageCardViewHolder){
            when {
                (position == 0) -> {
                    //holder.headImage.visibility = View.VISIBLE
                    holder.image.setImageUri(imageList[position])
                }
                else -> {
                    //holder.headImage.visibility = View.GONE
                    holder.image.setImageUri(imageList[position])
                    holder.image.setOnClickListener {

                    }
                }
            }
        }
        else {

        }
    }

    class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var image : ImageView = itemView.image
        var headImage : RelativeLayout = itemView.firstImage

    }

    class ImageCardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var image : ImageView = itemView.imageCard
    }

}