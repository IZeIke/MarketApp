package com.example.harit.marketapp.ui.loginPage

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.harit.marketapp.Application
import com.example.harit.marketapp.R
import com.example.harit.marketapp.extention.isEmailValid
import com.example.harit.marketapp.extention.isPasswordValid
import com.example.harit.marketapp.ui.feedPage.MainActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_signup.*
import java.util.*
import javax.inject.Inject



class SignupActivity : AppCompatActivity() {

    @Inject
    lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        Application.appComponent.inject(this)

        signupBtn.setOnClickListener {
            var name = nameEdt.text.toString()
            var email = emailEdt.text.toString().isEmailValid()
            var password = passwordEdt.text.toString().isPasswordValid()
            var passwordConfirm = passwordEdt.text.toString() == confirmPasswordEdt.text.toString()

            val user = HashMap<String,Any>()
            user.put("email", emailEdt.text.toString())
            user.put("name", name)
            user.put("username", emailEdt.text.toString())

            if(email && password && passwordConfirm) {

                mAuth.createUserWithEmailAndPassword(emailEdt.text.toString(), passwordEdt.text.toString())
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                user.put("id", it.result.user.uid)
                                FirebaseFirestore.getInstance().collection("Users").document(it.result.user.uid)
                                        .set(user).addOnSuccessListener {
                                            startActivity(Intent(this, MainActivity::class.java))
                                            finishAffinity()
                                        }
                            } else {
                                Toast.makeText(this,"failed to sign up",Toast.LENGTH_SHORT).show()
                            }
                        }
            }else {
                warningText.text = ""

                if(!email) {
                    warningText.text = warningText.text.toString() + " *email ไม่ถูกต้อง"
                }

                if(!password) {
                    warningText.text = warningText.text.toString() + " *password ต้องมีมากกว่า 5 ตัวขึ้นไป"
                }

                if(!passwordConfirm) {
                    warningText.text = warningText.text.toString() + " *password ไม่เหมือนกัน"
                }
            }
        }
    }
}
