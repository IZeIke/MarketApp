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
import com.example.harit.marketapp.ui.EditPage.EditPageActivity
import com.example.harit.marketapp.ui.NotiPage.NotiActivity
import com.example.harit.marketapp.ui.adapter.SellItemPageAdapter
import com.example.harit.marketapp.ui.chatPage.ChatListActivity
import com.example.harit.marketapp.ui.model.FeedModel
import com.example.harit.marketapp.ui.model.SearchModel
import com.example.harit.marketapp.ui.searchPage.FilterActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.fragment_sell.*

class SellFragment : Fragment(),SellItemPageAdapter.SellItemPageAdapterInterface {

    private var lastVisible : DocumentSnapshot? = null
    private val feedList : MutableList<FeedModel> = arrayListOf()

    companion object {
        fun newInstance(bundle: Bundle): SellFragment{
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

        setTopBar()

        fab.setOnClickListener {
            startActivity(Intent(activity,AddItemActivity::class.java))
        }

        //getData()
    }

    private fun getData() {
        showLoading()

        feedList.clear()

        var uid = FirebaseAuth.getInstance().currentUser?.uid
        var db = FirebaseFirestore.getInstance().collection("Feed")

        db.orderBy("create", Query.Direction.DESCENDING)
                .whereEqualTo("user.id",uid)
                .limit(20)
                .get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        Log.d("document", document.id + " => " + document.data)
                        val feedItem = document.toObject(FeedModel::class.java)
                        //feedItem.id = document.id
                        feedList.add(feedItem)
                    }

                    if(result.size() != 0)
                        lastVisible = result.documents[result.size() - 1]

                    setRecyclerView(feedList)
                    if(feedList.size == 0){
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
            it.adapter = SellItemPageAdapter(it.context, feedList,this)
            it.adapter?.notifyDataSetChanged()
            stopLoading()
            //swipeRefreshLayout.isRefreshing = false
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

    override fun updateSoldItem(id : String) {
        FirebaseFirestore.getInstance().collection("Feed").document(id)
                .update("status", "sold")
                .addOnSuccessListener {
                    getData()
                }
    }

    override fun deleteItem(id : String) {
        FirebaseFirestore.getInstance().collection("Feed").document(id)
                .delete()
                .addOnSuccessListener {
                    getData()
                }

    }

    override fun startEditActivity(feedModel: FeedModel) {
        startActivity(Intent(context,EditPageActivity::class.java).putExtra("model",feedModel))
    }

    private fun setTopBar() {
        topBar.setText("Sell")
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

        topBar.getNotiHolder()?.setOnClickListener {
            startActivity(Intent(context, NotiActivity::class.java))
        }
    }

    override fun onStart() {
        super.onStart()
        getData()
    }
}