package com.example.harit.marketapp.ui.loginPage

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.harit.marketapp.Application
import com.example.harit.marketapp.R
import com.example.harit.marketapp.ui.feedPage.MainActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import javax.inject.Inject
import android.content.Context
import android.view.inputmethod.InputMethodManager


class LoginActivity : AppCompatActivity() {

    @Inject
    lateinit var mAuth: FirebaseAuth
    lateinit var imm : InputMethodManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        Application.appComponent.inject(this)

        imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        initInstance()
    }

    private fun initInstance() {

        loginBtn.setOnClickListener {
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
            showLoading()
            var email = usernameEdt.text.toString()
            var password = passwordEdt.text.toString()

            mAuth.signInWithEmailAndPassword(email,password)
                    .addOnCompleteListener {
                        if(it.isSuccessful){

                            startActivity(Intent(this, MainActivity::class.java))
                            finish()

                        } else {
                            stopLoading()
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

    private fun showLoading(){
        loading.visibility = View.VISIBLE
        loginContainer.visibility = View.INVISIBLE
    }

    private fun stopLoading(){
        loading.visibility = View.GONE
        loginContainer.visibility = View.VISIBLE
    }
}
