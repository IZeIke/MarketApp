package com.example.harit.marketapp.ui.feedPage

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.harit.marketapp.R
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem
import com.example.harit.marketapp.ui.model.User
import com.example.harit.marketapp.ui.sellPage.SellFragment
import com.example.harit.marketapp.ui.settingPage.SettingFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    var uid = FirebaseAuth.getInstance().currentUser?.uid
    lateinit var bundle : Bundle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initInstance()

        if(savedInstanceState == null) {

            FirebaseFirestore.getInstance().collection("Users")
                    .document(uid!!).get()
                    .addOnCompleteListener {
                        var myUser = it.result.toObject(User::class.java)
                        bundle = Bundle().also {
                            it.putParcelable("myUser", myUser)
                        }
                        supportFragmentManager.beginTransaction()
                                .add(R.id.contentContainer, MarketViewpagerFragment.newInstance(bundle))
                                .commit()
                    }


        }
    }


    private fun initInstance() {

        navigationBarSetting()
    }

    private fun navigationBarSetting() {

        val item1 = AHBottomNavigationItem("Sell", R.drawable.ic_add_shopping_cart_white_24dp)
        val item2 = AHBottomNavigationItem("Market", R.drawable.ic_shopping_cart_black_24dp)
        val item3 = AHBottomNavigationItem("Setting", R.drawable.ic_settings_white_24dp)

        bottom_navigation.addItem(item1)
        bottom_navigation.addItem(item2)
        bottom_navigation.addItem(item3)

        bottom_navigation.defaultBackgroundColor = Color.parseColor("#008577")
        bottom_navigation.accentColor = Color.parseColor("#FFFFFF")
        bottom_navigation.inactiveColor = Color.parseColor("#66FFFFFF")
        bottom_navigation.isBehaviorTranslationEnabled = false
        bottom_navigation.isTranslucentNavigationEnabled = true
        bottom_navigation.currentItem = 1

        bottom_navigation.setOnTabSelectedListener { position, wasSelected ->
            if(!wasSelected){
                when (position) {
                    0 -> {
                        supportFragmentManager.beginTransaction()
                                .replace(R.id.contentContainer,SellFragment.newInstance(bundle))
                                .commit()
                    }
                    1 -> {
                        supportFragmentManager.beginTransaction()
                                .replace(R.id.contentContainer,MarketViewpagerFragment.newInstance(bundle))
                                .commit()
                    }
                    else -> {
                        supportFragmentManager.beginTransaction()
                                .replace(R.id.contentContainer,SettingFragment.newInstance(bundle))
                                .commit()
                    }
                }
            }
            true
        }

    }
}
