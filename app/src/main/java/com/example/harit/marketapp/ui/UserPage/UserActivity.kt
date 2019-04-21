package com.example.harit.marketapp.ui.UserPage

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.asksira.bsimagepicker.GridItemSpacingDecoration
import com.example.harit.marketapp.R
import com.example.harit.marketapp.extention.setImageUrl
import com.example.harit.marketapp.helper.RecyclerWithLoadMore
import com.example.harit.marketapp.ui.adapter.FeedPageAdapter
import com.example.harit.marketapp.ui.adapter.UserPageAdapter
import com.example.harit.marketapp.ui.chatPage.ChatActivity
import com.example.harit.marketapp.ui.model.FeedModel
import com.example.harit.marketapp.ui.model.User
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.activity_user.*

class UserActivity : AppCompatActivity() {

    lateinit var user : User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)

        user = intent.getParcelableExtra("user")

        initInstance()
    }

    private fun initInstance() {

        chatBtn.setOnClickListener {
            startActivity(Intent(this, ChatActivity::class.java).putExtra("user",user))
        }

        backHolder.setOnClickListener {
            onBackPressed()
        }

        sellerName.text = user.name
        user.imageUrl?.let {
            profile_image.setImageUrl(it)
        }
        var feedListSame = mutableListOf<FeedModel>()

        FirebaseFirestore.getInstance().collection("Feed").orderBy("create", Query.Direction.DESCENDING)
                .whereEqualTo("user.id",user?.id)
                .limit(8)
                .get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        Log.d("document", document.id + " => " + document.data)
                        val feedItem = document.toObject(FeedModel::class.java)
                        //feedItem.id = document.id
                        feedListSame.add(feedItem)
                    }

                    setRecyclerView(feedListSame)
                }
                .addOnFailureListener { exception ->
                    Log.d("document-error", "Error getting documents: ", exception)
                }
    }

    private fun setRecyclerView(feedList: MutableList<FeedModel>) {
        recyclerView?.let {
            recyclerView.layoutManager = GridLayoutManager(this,2)
            recyclerView.addItemDecoration(GridItemSpacingDecoration(2,20,true))
            recyclerView.adapter = UserPageAdapter(this, feedList)
            recyclerView.adapter?.notifyDataSetChanged()
            //stopLoading()
        }
    }


}