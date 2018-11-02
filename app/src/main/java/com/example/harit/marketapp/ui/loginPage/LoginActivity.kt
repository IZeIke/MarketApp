package com.example.harit.marketapp.ui.loginPage

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.harit.marketapp.Application
import com.example.harit.marketapp.R
import com.example.harit.marketapp.ui.feedPage.MainActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import javax.inject.Inject

class LoginActivity : AppCompatActivity() {

    @Inject
    lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        Application.appComponent.inject(this)

        initInstance()
    }

    private fun initInstance() {

        loginBtn.setOnClickListener {
            var email = usernameEdt.text.toString()
            var password = passwordEdt.text.toString()

            mAuth.signInWithEmailAndPassword(email,password)
                    .addOnCompleteListener {
                        if(it.isSuccessful){

                            startActivity(Intent(this, MainActivity::class.java))
                            finish()

                        } else {
                            Toast.makeText(this
                                    ,"email or password mistake"
                                    ,Toast.LENGTH_SHORT)
                                    .show()
                        }
                    }
        }


        signUpBtn.setOnClickListener {
            startActivity(Intent(this,SignupActivity::class.java))
        }
    }
}
