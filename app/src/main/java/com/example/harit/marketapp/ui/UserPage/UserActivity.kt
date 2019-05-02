package com.example.harit.marketapp.ui.UserPage

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.asksira.bsimagepicker.GridItemSpacingDecoration
import com.example.harit.marketapp.R
import com.example.harit.marketapp.extention.setImageUrl
import com.example.harit.marketapp.helper.RecyclerWithLoadMore
import com.example.harit.marketapp.ui.RatingPage.RatingActivity
import com.example.harit.marketapp.ui.adapter.FeedPageAdapter
import com.example.harit.marketapp.ui.adapter.UserPageAdapter
import com.example.harit.marketapp.ui.chatPage.ChatActivity
import com.example.harit.marketapp.ui.model.FeedModel
import com.example.harit.marketapp.ui.model.ScoreModel
import com.example.harit.marketapp.ui.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_user.*

class UserActivity : AppCompatActivity() {

    lateinit var user : User
    val uid = FirebaseAuth.getInstance().uid

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)

        user = intent.getParcelableExtra("user")

        var scoreModel : ScoreModel? = null
        var myUser : User? = null

        FirebaseFirestore.getInstance().collection("Users")
                .document(uid!!).get()
                .addOnCompleteListener {
                        myUser = it.result.toObject(User::class.java)
                    }

        FirebaseFirestore.getInstance().collection("Rating").document("score")
                .collection(user.id!!).document("score").get().addOnSuccessListener {
                    if(it.get("scoreSum") == 0 || it.get("scoreSum") == null){
                        score.text = (0).toDouble().toString()
                        scoreModel = ScoreModel(null,null)
                    }else{
                        scoreModel = it.toObject(ScoreModel::class.java)
                        score.text = (scoreModel?.scoreSum!! / scoreModel?.number!!).toString()
                    }
                }

        if(user.id == uid){
            chatBtn.visibility = View.GONE
        }else{
            subBtn.setOnClickListener {
                val intent = Intent(this,RatingActivity::class.java)
                intent.putExtra("model",scoreModel)
                intent.putExtra("myUser",myUser)
                intent.putExtra("user",user)
                startActivity(intent)
            }
        }

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