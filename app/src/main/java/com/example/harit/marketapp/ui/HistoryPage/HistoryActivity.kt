package com.example.harit.marketapp.ui.HistoryPage

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.asksira.bsimagepicker.GridItemSpacingDecoration
import com.example.harit.marketapp.R
import com.example.harit.marketapp.helper.RecyclerWithLoadMore
import com.example.harit.marketapp.ui.adapter.FeedPageAdapter
import com.example.harit.marketapp.ui.adapter.SellItemPageAdapter
import com.example.harit.marketapp.ui.model.FeedModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.activity_chat_list.*

class HistoryActivity : AppCompatActivity() {

    private val feedList : MutableList<FeedModel> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_list)

        initInstance()
    }

    private fun initInstance() {

        setTopbar()

        loadData()
    }

    private fun loadData() {
        showLoading()

        feedList.clear()

        var uid = FirebaseAuth.getInstance().currentUser?.uid
        var db = FirebaseFirestore.getInstance().collection("Feed")

        db.orderBy("create", Query.Direction.DESCENDING)
                .whereEqualTo("user.id",uid)
                .whereEqualTo("status","sold")
                .limit(20)
                .get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        Log.d("document", document.id + " => " + document.data)
                        val feedItem = document.toObject(FeedModel::class.java)
                        //feedItem.id = document.id
                        feedList.add(feedItem)
                    }

                    /*if(result.size() != 0)
                        lastVisible = result.documents[result.size() - 1]*/

                    setRecyclerView(feedList)
                    if(feedList.size == 0){
                        noItemText.text = "ไม่มีรายการขาย"
                        noItemText?.visibility = View.VISIBLE
                    }else{
                        noItemText?.visibility = View.GONE
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d("document-error", "Error getting documents: ", exception)
                    stopLoading()
                }
    }

    private fun setRecyclerView(feedList: MutableList<FeedModel>) {
        recyclerView?.let {
            var layoutManager = GridLayoutManager(it.context,2)
            it.layoutManager = layoutManager
            it.addOnScrollListener(object : RecyclerWithLoadMore(layoutManager)
            {
                override fun onLoadMore(current_page: Int) {
                    //getDataLoadMore()
                }
            })
            if(it.itemDecorationCount == 0){
                it.addItemDecoration(GridItemSpacingDecoration(2,20,true))
            }
            it.adapter = FeedPageAdapter(it.context, feedList)
            it.adapter?.notifyDataSetChanged()
            stopLoading()
            //swipeRefreshLayout.isRefreshing = false
        }
    }

    private fun setTopbar() {
        topBar.setText("Sold History")
        topBar.setChatNoti("0")
        topBar.setNoti("0")
        topBar.haveChat(false)
        topBar.haveNoti(false)
        topBar.haveSearch(false)
        topBar.haveBack(true)
        topBar.getBackHolder()?.setOnClickListener {
            onBackPressed()
        }
    }

    private fun showLoading(){
        loading.visibility = View.VISIBLE
        recyclerView.visibility = View.GONE
    }

    private fun stopLoading(){
        loading.visibility = View.GONE
        recyclerView.visibility = View.VISIBLE
    }
}