package com.example.harit.marketapp.ui.chatPage

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import com.example.harit.marketapp.R
import com.example.harit.marketapp.ui.adapter.ChatPageAdapter
import com.example.harit.marketapp.ui.itemPage.ItemPageActivity
import com.example.harit.marketapp.ui.model.Chat
import com.example.harit.marketapp.ui.model.FeedModel
import com.example.harit.marketapp.ui.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_chat.*

class ChatActivity : AppCompatActivity(),ChatPageAdapter.ChatPageAdapterListener {

    override fun onStartItemActivity(chat: Chat) {
        FirebaseFirestore.getInstance().collection("Feed")
                .document(chat?.messageType!!).get()
                .addOnCompleteListener {
                    var item = it.result.toObject(FeedModel::class.java)
                    this.startActivity(Intent(this, ItemPageActivity::class.java).putExtra("item",item))
                }
    }

    var feedItem : FeedModel? = null
    var user : User? = null
    lateinit var imm : InputMethodManager
    val uid = FirebaseAuth.getInstance().currentUser?.uid

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        if(intent.getParcelableExtra<FeedModel>("item") == null){
            user = intent.getParcelableExtra("user")
        }else{
            feedItem = intent.getParcelableExtra("item")
            user = feedItem?.user
        }

        initInstance()

        if(savedInstanceState == null) {
            FirebaseFirestore.getInstance().collection("Users")
                    .document(uid!!).get()
                    .addOnCompleteListener {
                        var myUser = it.result.toObject(User::class.java)
                        var chatId = getChatId(myUser?.nid!! , user?.nid!!)
                        var bundle = Bundle().also {
                            it.putString("chatId",chatId)
                            it.putParcelable("user",user)
                            it.putParcelable("myUser",myUser)
                        }
                        supportFragmentManager.beginTransaction()
                                .add(R.id.contentContainer,ChatFragment.newInstance(bundle))
                                .commit()
                    }

        }
    }

    private fun setTopbar() {
        topBar.haveNoti(false)
        topBar.haveSearch(false)
        topBar.setText(user?.name!!)
        topBar.haveBack(true)
        topBar.getBackHolder()?.setOnClickListener {
            onBackPressed()
        }
    }

    private fun initInstance() {
        setTopbar()
    }


    private fun getChatId(i: Int, j: Int): String {
        return if(i < j){
            i.toString()+"_"+j.toString()
        }else{
            j.toString()+"_"+i.toString()
        }
    }
}