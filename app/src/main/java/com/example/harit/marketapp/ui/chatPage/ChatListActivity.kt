package com.example.harit.marketapp.ui.chatPage

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.harit.marketapp.R
import com.example.harit.marketapp.ui.adapter.ChatListPageVerticalAdapter
import com.example.harit.marketapp.ui.model.ChatListModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.activity_chat_list.*

class ChatListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_list)

        initInstance()
    }

    private fun initInstance() {

        setTopbar()
    }

    private fun loadData() {
        FirebaseFirestore.getInstance().collection("ChatList").document("chatList")
                .collection(FirebaseAuth.getInstance().currentUser?.uid!!).orderBy("create", Query.Direction.DESCENDING)
                .get().addOnCompleteListener {
                    if(it.isSuccessful){
                        val chatList = mutableListOf<ChatListModel>()
                        for( doc in it.result ){
                            val chat = doc.toObject(ChatListModel::class.java)
                            chatList.add(chat)
                        }
                        if(chatList.size == 0){
                            noItemText.visibility = View.VISIBLE
                        }else{
                            noItemText.visibility = View.GONE
                        }
                        setRecyclerView(chatList)
                    }
                }
    }

    private fun setRecyclerView(chatList: MutableList<ChatListModel>) {
        recyclerView.layoutManager = LinearLayoutManager(this,RecyclerView.VERTICAL,false)
        recyclerView.adapter = ChatListPageVerticalAdapter(this,chatList)
        recyclerView.adapter?.notifyDataSetChanged()
    }

    private fun setTopbar() {
        topBar.setText("Chat")
        topBar.setChatNoti("0")
        topBar.setNoti("0")
        topBar.haveChat(false)
        topBar.haveBack(true)
        topBar.getBackHolder()?.setOnClickListener {
            onBackPressed()
        }
        topBar.getSearchHolder()?.setOnClickListener {
            /*var bundle = Bundle().also { bundle ->
                bundle.putParcelable("model", SearchModel())
                bundle.putInt("tag",1)
            }
            startActivity(Intent(context, FilterActivity::class.java)
                    .putExtras(bundle))*/
        }
    }

    override fun onResume() {
        super.onResume()
        loadData()
    }


}