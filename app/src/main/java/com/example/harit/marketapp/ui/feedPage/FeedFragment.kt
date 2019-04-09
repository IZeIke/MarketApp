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
import com.example.harit.marketapp.ui.adapter.FeedPageAdapter
import com.example.harit.marketapp.ui.model.FeedItem
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.fragment_feed.*

class FeedFragment : Fragment() {

    private val feedList : MutableList<FeedItem> = arrayListOf()

    companion object {
        fun newInstance(): FeedFragment{
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

        val db = FirebaseFirestore.getInstance()

        db.collection("Feed")
                .orderBy("create",Query.Direction.DESCENDING)
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
            feedRecyclerView.layoutManager = GridLayoutManager(context,2)
            feedRecyclerView.addItemDecoration(GridItemSpacingDecoration(2,20,true))
            feedRecyclerView.adapter = FeedPageAdapter(context!!, feedList)
            stopLoading()
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