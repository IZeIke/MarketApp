package com.example.harit.marketapp.ui.chatPage

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.harit.marketapp.R
import com.example.harit.marketapp.extention.Toast
import com.example.harit.marketapp.ui.adapter.ChatPageAdapter
import com.example.harit.marketapp.ui.model.Chat
import com.example.harit.marketapp.ui.model.ChatListModel
import com.example.harit.marketapp.ui.model.ChatModel
import com.example.harit.marketapp.ui.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_chat.*
import com.robertlevonyan.components.picker.set


class ChatFragment: Fragment() {

    //private var myUser = hashMapOf<String,String>()
    private val uid = FirebaseAuth.getInstance().currentUser?.uid
    private val mRootRef = FirebaseDatabase.getInstance().reference
    private val mMessagesRef = mRootRef.child("messages")
    private val fireStoreRef = FirebaseFirestore.getInstance()
    private var chatId : String = ""
    lateinit var user : User
    lateinit var myUser : User
    private var firstLock = true

    companion object {
        fun newInstance(bundle: Bundle): ChatFragment {
            var view = ChatFragment()
            view.arguments = bundle
            return view
        }
    }

    interface ChatFragmentInterface {
        fun closeKeyboard()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        chatId = arguments?.getString("chatId")!!
        user = arguments?.getParcelable("user")!!
        myUser = arguments?.getParcelable("myUser")!!


        /*FirebaseFirestore.getInstance().collection("Users")
                .document(uid!!).get()
                .addOnCompleteListener {
                    myUser = it.result.data as HashMap<String, String>
                    //chatId = getChatId((myUser["nid"] as Long).toInt() , 1)
                }*/
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return LayoutInflater.from(context).inflate(R.layout.fragment_chat,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showLoading()

        var myRecyclerView = recyclerView
        var lastKey = ""

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
                    chatModel.senderId = uid
                    chatModel.messageId = key
                    chatModel.create = ServerValue.TIMESTAMP
                }
                editText.set("")
                //(activity as ChatFragmentInterface).closeKeyboard()
                var chatList = ChatListModel().also { chatListModel ->
                    chatListModel.user = user
                    chatListModel.message = chatModel.message
                    chatListModel.messageType = chatModel.messageType
                }

                var myChatList = ChatListModel().also { chatListModel ->
                    chatListModel.user = myUser
                    chatListModel.message = chatModel.message
                    chatListModel.messageType = chatModel.messageType
                }

                mMessagesRef.child(chatId).child(key).setValue(chatModel)
                fireStoreRef.collection("ChatList").document("chatList")
                        .collection(user.id!!).document(uid!!).set(myChatList)
                fireStoreRef.collection("ChatList").document("chatList")
                        .collection(uid!!).document(user.id!!).set(chatList)
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
                /*Toast.makeText(context, "add messages.",
                        Toast.LENGTH_SHORT).show()*/
               /* if(myRecyclerView.adapter?.itemCount!! == 0){
                    val chat = dataSnapshot.getValue(Chat::class.java)
                    Log.d("chat",chat?.message)
                    (myRecyclerView.adapter as ChatPageAdapter).addList(chat!!)
                    //myRecyclerView.smoothScrollToPosition(myRecyclerView.adapter?.itemCount!! - 1)
                }else{*/
                    if(!firstLock){
                        val chat = dataSnapshot.getValue(Chat::class.java)
                        Log.d("chat",chat?.message)
                        (myRecyclerView.adapter as ChatPageAdapter).addList(chat!!)
                        myRecyclerView.smoothScrollToPosition(myRecyclerView.adapter?.itemCount!! - 1)
                    }else{
                        firstLock = false
                    }
               // }


            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        }

        mMessagesRef.child(chatId).addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                stopLoading()
                p0.message.Toast(activity!!)
            }

            override fun onDataChange(p0: DataSnapshot) {
                stopLoading()
                var chatList = mutableListOf<Chat>()
                for(data in p0.children){
                    val chat = data.getValue(Chat::class.java)
                    chatList.add(chat!!)
                    Log.d("chat",chat.message)
                    lastKey = chat?.messageId!!
                }

                myRecyclerView.let {
                    it.layoutManager = LinearLayoutManager(context,RecyclerView.VERTICAL,false).also { layoutManager ->
                        layoutManager.stackFromEnd = true
                    }
                    it.adapter = ChatPageAdapter(activity!!,uid!!,chatList)
                    it.adapter?.notifyDataSetChanged()
                }

                if(chatList.size == 0){
                    firstLock = false
                    mMessagesRef.child(chatId).addChildEventListener(childEventListener)
                }else {
                    mMessagesRef.child(chatId).orderByKey()
                            .startAt(lastKey).addChildEventListener(childEventListener)
                }
            }

        })

        /*mMessagesRef.child(chatId).addValueEventListener(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                for(data in p0.children){
                    val chat = data.getValue(Chat::class.java)
                    Log.d("chat",chat?.message)
                }
            }

        })*/

    }

    private fun getChatId(i: Int, j: Int): String {
        return if(i < j){
            i.toString()+"_"+j.toString()
        }else{
            j.toString()+"_"+i.toString()
        }
    }

    private fun showLoading(){
        loading.visibility = View.VISIBLE
        recyclerView.visibility = View.INVISIBLE
    }

    private fun stopLoading(){
        loading.visibility = View.GONE
        recyclerView.visibility = View.VISIBLE
    }

}