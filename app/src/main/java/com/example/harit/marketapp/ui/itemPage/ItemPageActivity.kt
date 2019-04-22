package com.example.harit.marketapp.ui.itemPage

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.harit.marketapp.ui.adapter.ItemPageAdapter
import com.example.harit.marketapp.ui.chatPage.ChatActivity
import com.example.harit.marketapp.ui.model.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ServerValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.activity_item.*
import android.util.DisplayMetrics
import com.example.harit.marketapp.R
import com.example.harit.marketapp.ui.EditPage.EditPageActivity


class ItemPageActivity : AppCompatActivity() {

    lateinit var feedItem: FeedModel
    var refresh = false
    var lock = true
    var myUser: User? = null
    val db = FirebaseFirestore.getInstance()
    var uid = FirebaseAuth.getInstance().currentUser?.uid
    private val mRootRef = FirebaseDatabase.getInstance().reference
    private val mMessagesRef = mRootRef.child("messages")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item)

        feedItem = intent.getParcelableExtra("item")
        //myUser = intent.getParcelableExtra("user")

        FirebaseFirestore.getInstance().collection("Users")
                .document(uid!!).get()
                .addOnCompleteListener {
                    myUser = it.result.toObject(User::class.java)
                }

        FirebaseFirestore.getInstance().collection("Book")
                .document("bookList").collection(uid!!).document(feedItem.id!!)
                .get()
                .addOnCompleteListener {
                    if(feedItem.status == "sold"){
                        tabEditHolder.visibility = View.VISIBLE
                        soldHolder.visibility = View.VISIBLE
                    }else {
                        if (it.result.get("id") != null) {
                            textBuyBtn.text = "จองแล้ว"
                            chatBtn.setBackgroundColor(ContextCompat.getColor(this, R.color.red))
                            chatBtn.isClickable = false
                        }
                        if (uid != feedItem.user?.id) {
                            tabHolder.visibility = View.VISIBLE
                        } else {
                            tabEditHolder.visibility = View.VISIBLE
                            editButton.setOnClickListener {
                                refresh = true
                                startActivity(Intent(this, EditPageActivity::class.java).putExtra("model",feedItem))
                            }
                            soldBtn.setOnClickListener {
                                FirebaseFirestore.getInstance().collection("Feed").document(feedItem.id!!)
                                        .update("status", "sold")
                                        .addOnSuccessListener {
                                            soldHolder.visibility = View.VISIBLE
                                        }
                            }
                        }
                    }
                }

        initInstance()
    }

    private fun initInstance() {

        getFeedListSame()
        getFeedListOther()

        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val width = displayMetrics.widthPixels

        recyclerView?.let { recyclerView ->
            recyclerView.layoutManager = LinearLayoutManager(this,RecyclerView.VERTICAL,false)
            recyclerView.adapter = ItemPageAdapter(this,feedItem,null,null,width/2).also {
                it.notifyDataSetChanged()
            }
        }

        btnInit()
    }

    private fun getFeedListSame() {
        var feedListSame = mutableListOf<FeedModel>()

        db.collection("Feed").orderBy("create", Query.Direction.DESCENDING)
                .whereEqualTo("user.id",feedItem.user?.id)
                .limit(8)
                .get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        Log.d("document", document.id + " => " + document.data)
                        if(document.id != feedItem.id) {
                            val feedItem = document.toObject(FeedModel::class.java)
                            //feedItem.id = document.id
                            if(feedItem.status != "sold") {
                                feedListSame.add(feedItem)
                            }
                        }
                    }

                    (recyclerView.adapter as ItemPageAdapter).addFeedListSame(feedListSame)
                }
                .addOnFailureListener { exception ->
                    Log.d("document-error", "Error getting documents: ", exception)
                }
    }

    private fun getFeedListOther() {
        var feedListOther = mutableListOf<FeedModel>()

        db.collection("Feed").orderBy("create", Query.Direction.DESCENDING)
                .whereEqualTo("filter.name", feedItem.filter!!["name"].toString())
                .limit(8)
                .get()
                .addOnSuccessListener {result ->
                    for(document in result){
                        if(document.id != feedItem.id) {
                            val feedItem = document.toObject(FeedModel::class.java)
                            //feedItem.id = document.id
                            if(feedItem.status != "sold") {
                                feedListOther.add(feedItem)
                            }
                        }
                    }
                    (recyclerView.adapter as ItemPageAdapter).addFeedListOther(feedListOther)
                }
                .addOnFailureListener { exception ->
                    Log.d("document-error", "Error getting documents: ", exception)
                }
    }

    private fun btnInit() {

        chatButton.setOnClickListener {
            startActivity(Intent(this, ChatActivity::class.java).putExtra("user",feedItem.user))
        }

        chatBtn.setOnClickListener {
            if(lock){
                lock = false
                textBuyBtn.text = "จองแล้ว"
                chatBtn.setBackgroundColor(ContextCompat.getColor(this, R.color.red))
                chatBtn.isClickable = false

                if(feedItem.status == "instock"){
                    feedItem.status = "1"
                }else{
                    feedItem.status = (feedItem.status?.toInt()!! + 1).toString()
                }
                val book = HashMap<String, Any>()
                book["id"] = feedItem.id!!

                val batch = db.batch()

                var bookRef = db.collection("Book").document("bookList").collection(uid!!).document(feedItem.id!!)

                batch.set(bookRef,book)

                var updateRef = db.collection("Feed").document(feedItem.id!!)

                batch.update(updateRef,"status",feedItem.status)

                batch.commit().addOnCompleteListener {
                    if(it.isSuccessful) {

                        var chatId = getChatId(myUser?.nid!!, feedItem.user?.nid!!)
                        var key = mMessagesRef.child(chatId).push().key!!
                        var chatModel = ChatModel().also { chatModel ->
                            chatModel.chatId = chatId
                            chatModel.mediaUrl = feedItem.imageUrl!![0]
                            chatModel.message = "${feedItem.name}_${myUser?.name} จองเป็นคิวที่ ${feedItem.status}"
                            chatModel.messageType = feedItem.id
                            chatModel.senderId = uid
                            chatModel.messageId = key
                            chatModel.create = ServerValue.TIMESTAMP
                        }
                        //(activity as ChatFragmentInterface).closeKeyboard()
                        var chatList = ChatListModel().also { chatListModel ->
                            chatListModel.user = feedItem.user
                            chatListModel.message = chatModel.message
                            chatListModel.messageType = chatModel.messageType
                        }

                        var myChatList = ChatListModel().also { chatListModel ->
                            chatListModel.user = myUser
                            chatListModel.message = chatModel.message
                            chatListModel.messageType = chatModel.messageType
                        }

                        mMessagesRef.child(chatId).child(key).setValue(chatModel)
                        db.collection("ChatList").document("chatList")
                                .collection(feedItem.user?.id!!).document(uid!!).set(myChatList)
                        db.collection("ChatList").document("chatList")
                                .collection(uid!!).document(feedItem.user?.id!!).set(chatList)

                        startActivity(Intent(this, ChatActivity::class.java).putExtra("item", feedItem))
                    }
                }
            }
        }

    }

    private fun getChatId(i: Int, j: Int): String {
        return if(i < j){
            i.toString()+"_"+j.toString()
        }else{
            j.toString()+"_"+i.toString()
        }
    }

    override fun onResume() {
        super.onResume()
        if (refresh) {
            getData()
        }
    }

    private fun getData() {
        FirebaseFirestore.getInstance().collection("Feed").document(feedItem.id!!)
                .get().addOnSuccessListener {
                    var item = it.toObject(FeedModel::class.java)
                    feedItem = item!!

                    (recyclerView.adapter as ItemPageAdapter).addFeedItem(feedItem)
                }
        refresh = false
    }


}