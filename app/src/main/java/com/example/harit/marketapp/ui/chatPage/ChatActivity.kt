package com.example.harit.marketapp.ui.chatPage

import android.content.Context
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import com.example.harit.marketapp.R
import com.example.harit.marketapp.ui.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ChatActivity : AppCompatActivity(),ChatFragment.ChatFragmentInterface {

    override fun closeKeyboard() {
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
    }

    lateinit var user : User
    lateinit var imm : InputMethodManager
    val uid = FirebaseAuth.getInstance().currentUser?.uid

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        user = intent.getParcelableExtra("user")

        initInstance()

        if(savedInstanceState == null) {
            FirebaseFirestore.getInstance().collection("Users")
                    .document(uid!!).get()
                    .addOnCompleteListener {
                        var myUser = it.result.toObject(User::class.java)
                        var chatId = getChatId(myUser?.nid!! , user.nid!!)
                        supportFragmentManager.beginTransaction()
                                .add(R.id.contentContainer,ChatFragment.newInstance(chatId))
                                .commit()
                    }

        }
    }

    private fun initInstance() {

    }


    private fun getChatId(i: Int, j: Int): String {
        return if(i < j){
            i.toString()+"_"+j.toString()
        }else{
            j.toString()+"_"+i.toString()
        }
    }
}