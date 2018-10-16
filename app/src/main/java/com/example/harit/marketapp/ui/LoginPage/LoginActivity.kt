package com.example.harit.marketapp.ui.LoginPage

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.harit.marketapp.Application
import com.example.harit.marketapp.R
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_login.*
import javax.inject.Inject

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        Application.appComponent.inject(this)

        signUpBtn.setOnClickListener {
            startActivity(Intent(this,SignupActivity::class.java))
        }

    }
}
