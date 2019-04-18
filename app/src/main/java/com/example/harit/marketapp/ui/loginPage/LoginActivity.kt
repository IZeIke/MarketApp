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
import android.util.Log
import android.view.inputmethod.InputMethodManager
import com.example.harit.marketapp.ui.model.User
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore


class LoginActivity : AppCompatActivity() {

    @Inject
    lateinit var mAuth: FirebaseAuth
    lateinit var imm : InputMethodManager
    lateinit var callbackManager : CallbackManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        Application.appComponent.inject(this)

        imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        initInstance()
    }

    private fun initInstance() {

        facebookLoginSetting()

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

    private fun facebookLoginSetting() {

        facebookLogin.setOnClickListener {
            login_button.performClick()
        }

        callbackManager = CallbackManager.Factory.create()

        login_button.setReadPermissions("email", "public_profile")
        login_button.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                Log.d("facebookLogin", "facebook:onSuccess:$loginResult")
                handleFacebookAccessToken(loginResult.accessToken)
            }

            override fun onCancel() {
                Log.d("facebookLogin", "facebook:onCancel")
                // ...
            }

            override fun onError(error: FacebookException) {
                Log.d("facebookLogin", "facebook:onError", error)
                // ...
            }
        })
    }

    private fun handleFacebookAccessToken(token: AccessToken) {
       // Log.d(TAG, "handleFacebookAccessToken:$token")

        val credential = FacebookAuthProvider.getCredential(token.token)
        FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        //Log.d(TAG, "signInWithCredential:success")
                        val user = FirebaseAuth.getInstance().currentUser
                        var isNewUser = task.result.additionalUserInfo.isNewUser
                        if(isNewUser){
                            updateUser(user)
                        }else{
                            startActivity(Intent(this, MainActivity::class.java))
                            finishAffinity()
                        }

                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w("signInWithCredential", "signInWithCredential:failure", task.exception)
                        Toast.makeText(baseContext, "Authentication failed.",
                                Toast.LENGTH_SHORT).show()
                        //updateUI(null)
                    }

                    // ...
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

    /*public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = FirebaseAuth.getInstance().currentUser
        updateUI(currentUser)
    }*/

    private fun updateUser(user: FirebaseUser?) {
        val myUser = User()
        myUser.name = user?.displayName
        myUser.email = user?.email
        myUser.id = user?.uid
        myUser.username = user?.email
        myUser.imageUrl = user?.photoUrl.toString()

        FirebaseFirestore.getInstance().collection("Users").get().addOnCompleteListener { task ->
            if(task.isSuccessful) {
                myUser.nid = task.result.size() + 1
                FirebaseFirestore.getInstance().collection("Users").document(user?.uid!!)
                        .set(myUser).addOnSuccessListener {
                            startActivity(Intent(this, MainActivity::class.java))
                            finishAffinity()
                        }
            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Pass the activity result back to the Facebook SDK
        callbackManager.onActivityResult(requestCode, resultCode, data)
    }
}
