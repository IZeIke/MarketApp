package com.example.harit.marketapp.ui.feedPage

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.asksira.bsimagepicker.GridItemSpacingDecoration
import com.example.harit.marketapp.R
import com.example.harit.marketapp.helper.RecyclerWithLoadMore
import com.example.harit.marketapp.ui.adapter.FeedPageAdapter
import com.example.harit.marketapp.ui.model.FeedModel
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.fragment_feed.*

class FeedFragment : Fragment() {

    private val feedList : MutableList<FeedModel> = arrayListOf()
    val db = FirebaseFirestore.getInstance()
    var lastVisible : DocumentSnapshot? = null

    companion object {
        fun newInstance(bundle: Bundle): FeedFragment{
            val fragment = FeedFragment()
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return LayoutInflater.from(context).inflate(R.layout.fragment_feed,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showLoading()

        getData()

        swipeRefreshLayout.setOnRefreshListener {
            feedList.clear()
            feedRecyclerView.adapter?.notifyDataSetChanged()
            getData()
        }


    }

    private fun getData() {

        db.collection("Feed")
                .orderBy("create",Query.Direction.DESCENDING)
                .limit(20)
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

                    setRecyclerView(feedList)
                    swipeRefreshLayout.isRefreshing = false
                }
                .addOnFailureListener { exception ->
                    Log.d("document-error", "Error getting documents: ", exception)
                }
    }

    private fun setRecyclerView(feedList: MutableList<FeedModel>) {
        feedRecyclerView?.let {
            var layoutManager = GridLayoutManager(context,2)
            feedRecyclerView.layoutManager = layoutManager
            feedRecyclerView.addOnScrollListener(object : RecyclerWithLoadMore(layoutManager)
            {
                override fun onLoadMore(current_page: Int) {
                    //"loadmore".Toast(this@FeedActivity)
                    getDataLoadMore()
                }
            })
            if(feedRecyclerView.itemDecorationCount == 0){
                feedRecyclerView.addItemDecoration(GridItemSpacingDecoration(2,20,true))
            }
            feedRecyclerView.adapter = FeedPageAdapter(context!!, feedList)
            stopLoading()
        }
    }

    private fun getDataLoadMore() {

        val feedList = mutableListOf<FeedModel>()
        db.collection("Feed")
                .orderBy("create",Query.Direction.DESCENDING)
                .startAfter(lastVisible!!)
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

                    (feedRecyclerView.adapter as FeedPageAdapter).addList(feedList)
                }
                .addOnFailureListener { exception ->
                    Log.d("document-error", "Error getting documents: ", exception)
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