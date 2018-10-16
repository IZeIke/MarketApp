package com.example.harit.marketapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.harit.marketapp.Application
import com.example.harit.marketapp.ui.LoginPage.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

class SplashScreenActivity : AppCompatActivity() {

    @Inject
    lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Application.appComponent.inject(this)

        if(mAuth.currentUser == null) {
            startActivity(
                    Intent(this, LoginActivity::class.java)
            )
            finish()
        }else{
            startActivity(
                    Intent(this, MainActivity::class.java)
            )
            finish()
        }
    }
}
