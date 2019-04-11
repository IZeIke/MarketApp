package com.example.harit.marketapp.ui.searchPage

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.asksira.bsimagepicker.GridItemSpacingDecoration
import com.example.harit.marketapp.R
import com.example.harit.marketapp.extention.Toast
import com.example.harit.marketapp.helper.RecyclerWithLoadMore
import com.example.harit.marketapp.ui.adapter.FeedPageAdapter
import com.example.harit.marketapp.ui.model.*
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.activity_feed.*


class FeedActivity : AppCompatActivity() {

    lateinit var model : SearchModel
    lateinit var dbRef: Query
    var lastVisible : DocumentSnapshot? = null
    private val feedList : MutableList<FeedItem> = arrayListOf()
    private var clickBar = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feed)

        showLoading()

        model = intent.getParcelableExtra("searchModel")

        setTopbar()
        setSwipeRefreshLayout()

        getData(model)
    }

    private fun setSwipeRefreshLayout() {
        swipeRefreshLayout.setOnRefreshListener {
            feedList.clear()
            feedRecyclerView.adapter?.notifyDataSetChanged()
            getData(model)
            showLoading()
        }
    }

    private fun getData(model: SearchModel) {

        feedList.clear()

        var db = FirebaseFirestore.getInstance().collection("Feed")

        var name = model.member
        var set = model.set
        var type = model.type

        dbRef = when(model?.sort){
            Sort.LASTED -> db
                    .orderBy("create", Query.Direction.DESCENDING)
            Sort.PRICEMORE -> db
                    .orderBy("price", Query.Direction.DESCENDING)
            Sort.PRICELESS -> db
                    .orderBy("price", Query.Direction.ASCENDING)
            else -> db
                    .orderBy("create", Query.Direction.DESCENDING)
        }

        if(name != "All") {
            dbRef = dbRef.whereEqualTo("filter.name", name)
        }

        if(set != 0){
            dbRef = dbRef.whereEqualTo("filter.set", "set $set")
        }

        if(type != Type.ALL && model.format != Format.SINGLE){
            dbRef = dbRef.whereEqualTo("filter.type", type)
        }


            dbRef.whereEqualTo("filter.photosetType",model.format)
                    .limit(20)
                .get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        Log.d("document", document.id + " => " + document.data)
                        val feedItem = document.toObject(FeedItem::class.java)
                        feedList.add(feedItem)
                    }

                    if(result.size() != 0)
                        lastVisible = result.documents[result.size() - 1]

                    setRecyclerView(feedList)
                    if(feedList.size == 0){
                        noItemText.visibility = View.VISIBLE
                    }else{
                        noItemText.visibility = View.GONE
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d("document-error", "Error getting documents: ", exception)
                }
    }

    private fun setRecyclerView(feedList: MutableList<FeedItem>) {
        feedRecyclerView?.let {
            var layoutManager = GridLayoutManager(this,2)
            feedRecyclerView.layoutManager = layoutManager
            feedRecyclerView.addOnScrollListener(object : RecyclerWithLoadMore(layoutManager)
            {
                override fun onLoadMore(current_page: Int) {
                    "loadmore".Toast(this@FeedActivity)
                    getDataLoadMore()
                }
            })
            if(feedRecyclerView.itemDecorationCount == 0){
                feedRecyclerView.addItemDecoration(GridItemSpacingDecoration(2,20,true))
            }
            feedRecyclerView.adapter = FeedPageAdapter(this, feedList)
            feedRecyclerView.adapter?.notifyDataSetChanged()
            stopLoading()
            swipeRefreshLayout.isRefreshing = false
        }
    }

    private fun getDataLoadMore() {

        val feedList = mutableListOf<FeedItem>()
        dbRef.whereEqualTo("filter.photosetType",model.format)
                .startAfter(lastVisible!!)
                .limit(20)
                .get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        Log.d("document", document.id + " => " + document.data)
                        val feedItem = document.toObject(FeedItem::class.java)
                        feedList.add(feedItem)
                    }

                    if(result.size() != 0){
                        lastVisible = result.documents[result.size() - 1]
                    }

                    (feedRecyclerView.adapter as FeedPageAdapter).addList(feedList)
                }
                .addOnFailureListener { exception ->
                    Log.d("document-error", "Error getting documents: ", exception)
                }

    }

    private fun setTopbar() {
        topBar?.let {
            it.setText("Search")
            it.haveNoti(false)
            it.haveSearch(false)
            it.haveFilter(true)
            it.getFilterHolder()?.setOnClickListener {
                if(!clickBar) {
                    clickBar = true
                    var bundle = Bundle().also { bundle ->
                        bundle.putParcelable("model", model)
                        bundle.putInt("tag", 2)
                    }
                    startActivityForResult(Intent(this, FilterActivity::class.java).putExtras(bundle), 1)
                }
        }
    }
}

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 1){
            if(resultCode == Activity.RESULT_OK){
                noItemText.visibility = View.GONE
                clickBar = false
                showLoading()
                model = data?.getParcelableExtra("searchModel")!!
                getData(model)
            }else{

            }
        }
    }

    private fun showLoading(){
        loading.visibility = View.VISIBLE
        feedRecyclerView.visibility = View.GONE
    }

    private fun stopLoading(){
        loading.visibility = View.GONE
        feedRecyclerView.visibility = View.VISIBLE
    }

}