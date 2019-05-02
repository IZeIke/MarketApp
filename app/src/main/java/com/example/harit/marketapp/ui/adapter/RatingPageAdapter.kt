package com.example.harit.marketapp.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.harit.marketapp.R
import com.example.harit.marketapp.extention.setImageUrl
import com.example.harit.marketapp.ui.model.RatingModel
import com.example.harit.marketapp.ui.model.ScoreModel
import com.example.harit.marketapp.ui.model.User
import kotlinx.android.synthetic.main.item_add_rating.view.*
import kotlinx.android.synthetic.main.item_rating.view.*
import kotlinx.android.synthetic.main.item_review.view.*

class RatingPageAdapter(private val listener: RatingPageAdapterInterface, private val myUser : User, private val scoreModel: ScoreModel, private val feedList: MutableList<RatingModel>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    interface RatingPageAdapterInterface{
        fun onAddReview(ratingModel: RatingModel)
        fun onUpdateReview(ratingModel: RatingModel)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if(viewType == 0) {
            return SummaryViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_review, parent, false))
        }
        if(viewType == 1){
            return AddViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_add_rating, parent, false))
        }
        else{
            return ReviewViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_rating, parent, false))
        }
    }

    override fun getItemCount(): Int {
        return feedList.size + 2
    }

    override fun getItemViewType(position: Int): Int {
        return if(position == 0){
            0
        }else if(position == 1){
            1
        }else{
            2
        }
    }

    fun addList(list : RatingModel){

        feedList.add(list)

        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is AddViewHolder){
            var isEdit = false
            var rateModel = RatingModel()
            for(model in feedList){
                if(model.user?.id == myUser.id){
                    isEdit = true
                    rateModel = model
                }
            }
            if(isEdit){
                holder.addBtn.text = "แก้ไขรีวิว"
                holder.rateing.rating = rateModel.score?.toFloat()!!
                holder.reviewEdt.setText(rateModel.review)
                holder.addBtn.setOnClickListener {
                    var rate = holder.rateing.rating.toDouble()
                    var review = holder.reviewEdt.text.toString()
                    listener.onUpdateReview(RatingModel(rate, review, myUser))
                }
            }else {

                holder.addBtn.setOnClickListener {
                    var rate = holder.rateing.rating.toDouble()
                    var review = holder.reviewEdt.text.toString()
                    listener.onAddReview(RatingModel(rate, review, myUser))
                }
            }

        }else if(holder is SummaryViewHolder){
            if(scoreModel?.number == null){
                holder.ratingBar.rating = (0).toFloat()
                holder.reviewNumber.text = "0"
                holder.scoreText.text = "0.0"
            }else {
                holder.ratingBar.rating = (scoreModel?.scoreSum!! / scoreModel?.number!!).toFloat()
                holder.reviewNumber.text = scoreModel.number.toString()
                holder.scoreText.text = (scoreModel?.scoreSum!! / scoreModel?.number!!).toString()
            }
        }else{
            if(holder is ReviewViewHolder){
                holder.nameText.text = feedList[position-2].user?.name
                feedList[position-2].user?.imageUrl?.let {
                    holder.profile_image.setImageUrl(it)
                }
                holder.rateing.rating = feedList[position-2].score?.toFloat()!!
                holder.reviewText.text = feedList[position-2].review
            }
        }
    }

    class SummaryViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        //var ratingBar = itemView.rating_reviews
        var ratingBar = itemView.ratingBar
        var scoreText = itemView.scoreText
        var reviewNumber = itemView.reviewNumber
    }

    class AddViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        var rateing = itemView.rating_1
        var reviewEdt = itemView.reviewEdt
        var addBtn = itemView.addBtn
    }

    class ReviewViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        var rateing = itemView.rating
        var profile_image = itemView.profile_image
        var reviewText = itemView.reviewText
        var nameText = itemView.nameText
    }

}