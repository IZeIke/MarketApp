package com.example.harit.marketapp.ui.searchPage

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.asksira.bsimagepicker.GridItemSpacingDecoration
import com.example.harit.marketapp.R
import com.example.harit.marketapp.ui.adapter.FeedPageAdapter
import com.example.harit.marketapp.ui.model.FeedItem
import com.example.harit.marketapp.ui.model.SearchModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.activity_feed.*

class FeedActivity : AppCompatActivity() {

    private val feedList : MutableList<FeedItem> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feed)

        var model = intent.getParcelableExtra<SearchModel>("searchModel")

        setTopbar()

        getData()
    }

    private fun getData() {
        val db = FirebaseFirestore.getInstance()

        db.collection("Feed")
                .orderBy("create", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        Log.d("document", document.id + " => " + document.data)
                        val feedItem = document.toObject(FeedItem::class.java)
                        feedList.add(feedItem)
                    }

                    setRecyclerView(feedList)
                }
                .addOnFailureListener { exception ->
                    Log.d("document-error", "Error getting documents: ", exception)
                }
    }

    private fun setRecyclerView(feedList: MutableList<FeedItem>) {
        feedRecyclerView?.let {
            feedRecyclerView.layoutManager = GridLayoutManager(this,2)
            feedRecyclerView.addItemDecoration(GridItemSpacingDecoration(2,20,true))
            feedRecyclerView.adapter = FeedPageAdapter(this, feedList)
        }
    }

    private fun setTopbar() {
        topBar?.let {
            it.setText("Search")
            it.haveNoti(false)
            it.haveSearch(false)
            it.haveFilter(true)
            it.getFilterHolder()?.setOnClickListener {
                startActivityForResult(Intent(this,FilterActivity::class.java),1)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 1){
            //getData()
        }
    }

}