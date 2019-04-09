package com.example.harit.marketapp.ui.feedPage

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.harit.marketapp.R
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem
import com.example.harit.marketapp.ui.sellPage.SellFragment
import com.example.harit.marketapp.ui.settingPage.SettingFragment
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initInstance()

        if(savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .add(R.id.contentContainer,MarketViewpagerFragment.newInstance())
                    .commit()
        }
    }

    private fun initInstance() {

        navigationBarSetting()
    }

    private fun navigationBarSetting() {

        val item1 = AHBottomNavigationItem("Sell", R.drawable.ic_shopping_cart_black_24dp)
        val item2 = AHBottomNavigationItem("Market", R.drawable.ic_shopping_cart_black_24dp)
        val item3 = AHBottomNavigationItem("Setting", R.drawable.ic_shopping_cart_black_24dp)

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
                                .replace(R.id.contentContainer,SellFragment.newInstance())
                                .commit()
                    }
                    1 -> {
                        supportFragmentManager.beginTransaction()
                                .replace(R.id.contentContainer,MarketViewpagerFragment.newInstance())
                                .commit()
                    }
                    else -> {
                        supportFragmentManager.beginTransaction()
                                .replace(R.id.contentContainer,SettingFragment.newInstance())
                                .commit()
                    }
                }
            }
            true
        }

    }
}
