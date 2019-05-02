package com.example.harit.marketapp.data.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.harit.marketapp.extention.Resource
import com.example.harit.marketapp.ui.model.FeedModel
import com.example.harit.marketapp.ui.model.FeedValueModel
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class FeedRepository {

    private val db = FirebaseFirestore.getInstance()

    fun getLastedFeed() : MutableLiveData<Resource<FeedValueModel>>{

        var feedValue = MutableLiveData<Resource<FeedValueModel>>()
        var feedList = mutableListOf<FeedModel>()
        var lastVisible : DocumentSnapshot? = null
        var dbRef = db.collection("Feed")
                .orderBy("create", Query.Direction.DESCENDING)

        dbRef.limit(20)
                .get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        Log.d("document", document.id + " => " + document.data)
                        val feedItem = document.toObject(FeedModel::class.java)
                        //feedItem.id = document.id
                        //if(feedItem.status != "sold") {
                        feedList.add(feedItem)
                        //}
                    }

                    if(result.size() != 0)
                        lastVisible = result.documents[result.size() - 1]

                    //setRecyclerView(feedList)
                    //swipeRefreshLayout?.isRefreshing = false
                    feedValue.value = Resource.success(FeedValueModel(feedList,lastVisible))
                }
                .addOnFailureListener { exception ->
                    Log.d("document-error", "Error getting documents: ", exception)
                    feedValue.value = Resource.error("Error getting documents",null)
                }

        return feedValue
    }

    fun getLastedFeedLoadMore(last : DocumentSnapshot) : MutableLiveData<Resource<FeedValueModel>>{

        var feedValue = MutableLiveData<Resource<FeedValueModel>>()
        var lastVisible = last
        var dbRef = db.collection("Feed")
                .orderBy("create", Query.Direction.DESCENDING)

        val feedList = mutableListOf<FeedModel>()
        dbRef.startAfter(lastVisible!!)
                .limit(20)
                .get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        Log.d("document", document.id + " => " + document.data)
                        val feedItem = document.toObject(FeedModel::class.java)
                        //if(feedItem.status != "sold") {
                        feedList.add(feedItem)
                        //}
                    }

                    if(result.size() != 0){
                        lastVisible = result.documents[result.size() - 1]
                    }

                    feedValue.value = Resource.success(FeedValueModel(feedList,lastVisible))
                    //(feedRecyclerView.adapter as FeedPageAdapter).addList(feedList)
                }
                .addOnFailureListener { exception ->
                    Log.d("document-error", "Error getting documents: ", exception)
                    feedValue.value = Resource.error("Error getting documents",null)
                }
        return feedValue
    }

    fun getFeedBySearch(){

    }

    fun getFeedBySearchLoadMore(){


    }

    fun getMyItemFeed(){

    }

}