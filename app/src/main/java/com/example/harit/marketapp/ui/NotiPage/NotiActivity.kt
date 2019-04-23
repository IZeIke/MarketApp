package com.example.harit.marketapp.ui.NotiPage

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.harit.marketapp.R
import com.example.harit.marketapp.ui.adapter.NotiListPageAdapter
import com.example.harit.marketapp.ui.model.NotiModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.activity_chat_list.*

class NotiActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_list)

        initInstance()
    }

    private fun initInstance() {

        setTopbar()
    }

    private fun loadData() {
        FirebaseFirestore.getInstance().collection("Noti").document("notiList")
                .collection(FirebaseAuth.getInstance().currentUser?.uid!!).orderBy("create", Query.Direction.DESCENDING)
                .get().addOnCompleteListener {
                    if(it.isSuccessful){
                        val notiList = mutableListOf<NotiModel>()
                        for( doc in it.result ){
                            val noti = doc.toObject(NotiModel::class.java)
                            notiList.add(noti)
                        }
                        if(notiList.size == 0){
                            noNotiText.visibility = View.VISIBLE
                        }else{
                            noNotiText.visibility = View.GONE
                        }
                        setRecyclerView(notiList)
                    }
                }
    }

    private fun setRecyclerView(notiList: MutableList<NotiModel>) {
        recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL,false)
        recyclerView.adapter = NotiListPageAdapter(this,notiList)
        recyclerView.adapter?.notifyDataSetChanged()
    }

    private fun setTopbar() {
        topBar.setText("Notification")
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

    override fun onResume() {
        super.onResume()
        loadData()
    }
}