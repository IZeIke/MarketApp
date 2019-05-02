package com.example.harit.marketapp.ui.feedPage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.harit.marketapp.data.repository.FeedRepository
import com.example.harit.marketapp.extention.Resource
import com.example.harit.marketapp.ui.model.FeedValueModel
import com.google.firebase.firestore.DocumentSnapshot

class FeedViewModel : ViewModel() {

    private val feedRepository = FeedRepository()

    private val getFeedInput = MutableLiveData<Boolean>()
    private val getFeedInputLoadMore = MutableLiveData<DocumentSnapshot>()

    private var feedData = Transformations.switchMap(getFeedInput) {
        feedRepository.getLastedFeed()
    }

    private var feedLoadMoreData = Transformations.switchMap(getFeedInputLoadMore){
        lastVisible -> feedRepository.getLastedFeedLoadMore(lastVisible)
    }

    fun getFeed(){
        getFeedInput.value = true
    }

    fun getFeedInputLoadMore(lastVisible : DocumentSnapshot){
        getFeedInputLoadMore.value = lastVisible
    }

    fun getFeedData(): LiveData<Resource<FeedValueModel>> {
        return feedData
    }

    fun getFeedLoadMoreData(): LiveData<Resource<FeedValueModel>> {
        return feedLoadMoreData
    }
}