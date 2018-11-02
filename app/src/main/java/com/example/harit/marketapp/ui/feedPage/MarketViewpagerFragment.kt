package com.example.harit.marketapp.ui.feedPage

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.harit.marketapp.R
import com.example.harit.marketapp.ui.loginPage.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_market_viewpager.*


class MarketViewpagerFragment : Fragment() {

    companion object {
        fun newInstance() : MarketViewpagerFragment{
            val fragment = MarketViewpagerFragment()
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return LayoutInflater.from(context).inflate(R.layout.fragment_market_viewpager,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        topBar.setText("Market")
        topBar.setChatNoti("0")
        topBar.setNoti("0")

        signoutBtn.setOnClickListener {
            FirebaseAuth.getInstance().signOut().also {
                startActivity(Intent(activity,LoginActivity::class.java))
                (activity as MainActivity).finish()
            }
        }
    }

}
