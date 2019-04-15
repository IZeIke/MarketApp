package com.example.harit.marketapp.ui.chatPage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.harit.marketapp.R
import com.example.harit.marketapp.ui.model.Chat
import com.example.harit.marketapp.ui.model.ChatModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_chat.*
import com.google.firebase.firestore.FirebaseFirestore


class ChatFragment: Fragment() {

    private var myUser = hashMapOf<String,String>()
    private val uid = FirebaseAuth.getInstance().currentUser?.uid
    private val mRootRef = FirebaseDatabase.getInstance().reference
    private val mMessagesRef = mRootRef.child("messages")
    private var chatId : String = "1_6"

    companion object {
        fun newInstance(): ChatFragment {
            var view = ChatFragment()
            return view
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        FirebaseFirestore.getInstance().collection("Users")
                .document(uid!!).get()
                .addOnCompleteListener {
                    myUser = it.result.data as HashMap<String, String>
                    //chatId = getChatId((myUser["nid"] as Long).toInt() , 1)
                }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return LayoutInflater.from(context).inflate(R.layout.fragment_chat,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ic.setOnClickListener {
            if (editText.text.toString().trim().isEmpty())
            {

            }else{
                var key = mMessagesRef.child(chatId).push().key!!
                var chatModel = ChatModel().also {chatModel ->
                    chatModel.chatId = chatId
                    chatModel.mediaUrl = ""
                    chatModel.message = editText.text.toString()
                    chatModel.messageType = "TEXT"
                    chatModel.senderId = myUser["id"] as String
                    chatModel.messageId = key
                    chatModel.create = ServerValue.TIMESTAMP
                }
                mMessagesRef.child(chatId).child(key).setValue(chatModel)
            }
        }

        val childEventListener = object : ChildEventListener {
            override fun onChildMoved(p0: DataSnapshot, p1: String?) {

            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {

            }

            override fun onChildRemoved(p0: DataSnapshot) {

            }

            override fun onChildAdded(dataSnapshot: DataSnapshot, previousChildName: String?) {
                Toast.makeText(context, "add messages.",
                        Toast.LENGTH_SHORT).show()
                val chat = dataSnapshot.getValue(Chat::class.java)
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        }

        mMessagesRef.child(chatId).addChildEventListener(childEventListener)

        recyclerView.let {
            it.layoutManager = LinearLayoutManager(context,RecyclerView.VERTICAL,true)
            //it.adapter = FeedPageAdapter(context!!, chatList)
        }

    }

    private fun getChatId(i: Int, j: Int): String {
        return if(i < j){
            i.toString()+"_"+j.toString()
        }else{
            j.toString()+"_"+i.toString()
        }
    }

}