package com.example.harit.marketapp.ui.sellPage

import android.content.Intent
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
import com.example.harit.marketapp.ui.model.FeedItem
import com.example.harit.marketapp.ui.model.SearchModel
import com.example.harit.marketapp.ui.searchPage.FilterActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_sell.*

class SellFragment : Fragment() {

    private var lastVisible : DocumentSnapshot? = null
    private val feedList : MutableList<FeedItem> = arrayListOf()

    companion object {
        fun newInstance() : SellFragment{
            val fragment = SellFragment()
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return LayoutInflater.from(context).inflate(R.layout.fragment_sell,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        topBar.getSearchHolder()?.setOnClickListener {
            var bundle = Bundle().also { bundle ->
                bundle.putParcelable("model", SearchModel())
                bundle.putInt("tag",1)
            }
            startActivity(Intent(context, FilterActivity::class.java)
                    .putExtras(bundle))
        }

        fab.setOnClickListener {
            startActivity(Intent(activity,AddItemActivity::class.java))
        }

        getData()
    }

    private fun getData() {

        var uid = FirebaseAuth.getInstance().currentUser?.uid
        var db = FirebaseFirestore.getInstance().collection("Feed")

        db.whereEqualTo("user.id",uid)
                .limit(20)
                .get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        Log.d("document", document.id + " => " + document.data)
                        val feedItem = document.toObject(FeedItem::class.java)
                        feedList.add(feedItem)
                    }

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
            //swipeRefreshLayout.isRefreshing = false
        }
    }

}