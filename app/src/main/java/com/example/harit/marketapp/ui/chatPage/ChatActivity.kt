package com.example.harit.marketapp.ui.chatPage

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.harit.marketapp.R

class ChatActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        initInstance()

        if(savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .add(R.id.contentContainer,ChatFragment.newInstance())
                    .commit()
        }
    }

    private fun initInstance() {

    }
}