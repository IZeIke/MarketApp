package com.example.harit.marketapp.ui.settingPage

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.harit.marketapp.R
import com.example.harit.marketapp.ui.feedPage.MainActivity
import com.example.harit.marketapp.ui.loginPage.LoginActivity
import com.example.harit.marketapp.ui.model.SearchModel
import com.example.harit.marketapp.ui.searchPage.FilterActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_setting.*

class SettingFragment : Fragment() {

    companion object {
        fun newInstance() : SettingFragment {
            val fragment = SettingFragment()
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return LayoutInflater.from(context).inflate(R.layout.fragment_setting,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTopBar()

        logOutBtn.setOnClickListener {
            FirebaseAuth.getInstance().signOut().also {
                startActivity(Intent(activity, LoginActivity::class.java))
                (activity as MainActivity).finish()
            }
        }
    }

    private fun setTopBar() {
        topBar.setText("Setting")
        topBar.setChatNoti("0")
        topBar.setNoti("0")
        topBar.getSearchHolder()?.setOnClickListener {
            var bundle = Bundle().also { bundle ->
                bundle.putParcelable("model", SearchModel())
                bundle.putInt("tag",1)
            }
            startActivity(Intent(context, FilterActivity::class.java)
                    .putExtras(bundle))
        }
    }
}