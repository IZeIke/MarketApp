package com.example.harit.marketapp.ui.feedPage

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.asksira.bsimagepicker.GridItemSpacingDecoration
import com.example.harit.marketapp.R
import com.example.harit.marketapp.helper.RecyclerWithLoadMore
import com.example.harit.marketapp.ui.adapter.FeedPageAdapter
import com.example.harit.marketapp.ui.adapter.MainPageAdapter
import com.example.harit.marketapp.ui.chatPage.ChatListActivity
import com.example.harit.marketapp.ui.model.FeedModel
import com.example.harit.marketapp.ui.model.SearchModel
import com.example.harit.marketapp.ui.model.User
import com.example.harit.marketapp.ui.searchPage.FilterActivity
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.fragment_sell.*


class MarketViewpagerFragment : Fragment() {

    private val feedList : MutableList<FeedModel> = arrayListOf()
    lateinit var myUser : User
    lateinit var dbRef : Query
    var lastVisible : DocumentSnapshot? = null
    val db = FirebaseFirestore.getInstance()

    companion object {
        fun newInstance(bundle: Bundle) : MarketViewpagerFragment{
            val fragment = MarketViewpagerFragment()
            fragment.arguments = bundle
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
        setTopBar()

        myUser = arguments?.getParcelable("myUser")!!
        var bundle = Bundle().also {
            it.putParcelable("myUser", myUser)
        }

        showLoading()

        fab.visibility = View.GONE

        dbRef = db.collection("Feed")
                .orderBy("create", Query.Direction.DESCENDING)

        getData()

        swipeRefreshLayout?.setOnRefreshListener {
            feedList.clear()
            recyclerView.adapter?.notifyDataSetChanged()
            getData()
        }

        /*var adapter = MainPageAdapter(childFragmentManager)
        adapter.setFragmentItem(FeedFragment.newInstance(bundle,0)
                , 0, "Set")
        viewPager?.adapter = adapter
        initTab()*/
    }

    private fun initTab() {
        /*tabBar.setSelectedTabIndicatorColor(ContextCompat.getColor(tabBar.context, R.color.white))
        tabBar.setTabTextColors(ContextCompat.getColor(tabBar.context, R.color.white40)
                , ContextCompat.getColor(tabBar.context, R.color.white))
        tabBar.setupWithViewPager(viewPager)*/
    }

    private fun getData() {

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

                    setRecyclerView(feedList)
                    swipeRefreshLayout?.isRefreshing = false
                }
                .addOnFailureListener { exception ->
                    Log.d("document-error", "Error getting documents: ", exception)
                }
    }

    private fun setRecyclerView(feedList: MutableList<FeedModel>) {
        recyclerView?.let {
            var layoutManager = GridLayoutManager(context,2)
            recyclerView.layoutManager = layoutManager
            recyclerView.addOnScrollListener(object : RecyclerWithLoadMore(layoutManager)
            {
                override fun onLoadMore(current_page: Int) {
                    //"loadmore".Toast(this@FeedActivity)
                    getDataLoadMore()
                }
            })
            if(recyclerView.itemDecorationCount == 0){
                recyclerView.addItemDecoration(GridItemSpacingDecoration(2,20,true))
            }
            recyclerView.adapter = FeedPageAdapter(context!!, feedList)
            stopLoading()
        }
    }

    private fun getDataLoadMore() {

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

                    (recyclerView.adapter as FeedPageAdapter).addList(feedList)
                }
                .addOnFailureListener { exception ->
                    Log.d("document-error", "Error getting documents: ", exception)
                }

    }

    private fun setTopBar() {
        topBar.setText("Market")
        topBar.setChatNoti("0")
        topBar.setNoti("0")
        topBar.haveSearch(true)
        topBar.getSearchHolder()?.setOnClickListener {
            var bundle = Bundle().also { bundle ->
                bundle.putParcelable("model",SearchModel())
                bundle.putInt("tag",1)
            }
            startActivity(Intent(context,FilterActivity::class.java)
                    .putExtras(bundle))
        }

        topBar.getChatHolder()?.setOnClickListener {
            startActivity(Intent(context,ChatListActivity::class.java))
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
