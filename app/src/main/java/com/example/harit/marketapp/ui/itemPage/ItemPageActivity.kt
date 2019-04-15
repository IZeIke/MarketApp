package com.example.harit.marketapp.ui.itemPage

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.harit.marketapp.R
import com.example.harit.marketapp.extention.setImageUrl
import com.example.harit.marketapp.ui.chatPage.ChatActivity
import com.example.harit.marketapp.ui.model.FeedItem
import com.example.harit.marketapp.ui.model.FeedModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_item.*

class ItemPageActivity : AppCompatActivity() {

    lateinit var feedItem: FeedModel
    var uid = FirebaseAuth.getInstance().currentUser?.uid

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item)

        feedItem = intent.getParcelableExtra("item")

        initInstance()
    }

    private fun initInstance() {

        if(uid == feedItem.user?.id){
            chatBtn.visibility = View.GONE
        }

        sellerName.text = feedItem.user?.name
        itemImage.setImageUrl(feedItem.imageUrl!![0])
        itemPrice.text = "฿"+feedItem.price.toString()
        itemName.text = feedItem.name
        itemDes.text = feedItem.description

        chatButton.setOnClickListener {
            startActivity(Intent(this, ChatActivity::class.java).putExtra("user",feedItem.user))
        }
    }

}