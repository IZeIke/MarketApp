package com.example.harit.marketapp.ui.feedPage

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.harit.marketapp.R
import com.example.harit.marketapp.ui.adapter.MainPageAdapter
import com.example.harit.marketapp.ui.chatPage.ChatListActivity
import com.example.harit.marketapp.ui.model.SearchModel
import com.example.harit.marketapp.ui.model.User
import com.example.harit.marketapp.ui.searchPage.FilterActivity
import kotlinx.android.synthetic.main.fragment_market_viewpager.*


class MarketViewpagerFragment : Fragment() {

    lateinit var myUser : User

    companion object {
        fun newInstance(bundle: Bundle) : MarketViewpagerFragment{
            val fragment = MarketViewpagerFragment()
            fragment.arguments = bundle
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
        setTopBar()

        myUser = arguments?.getParcelable("myUser")!!
        var bundle = Bundle().also {
            it.putParcelable("myUser", myUser)
        }

        var adapter = MainPageAdapter(childFragmentManager)
        adapter.setFragmentItem(FeedFragment.newInstance(bundle)
                , 0, "Buy")
        adapter.setFragmentItem(FeedFragment.newInstance(bundle)
                , 1, "Trade")
        viewPager?.adapter = adapter
        initTab()
    }

    private fun initTab() {
        tabBar.setSelectedTabIndicatorColor(ContextCompat.getColor(tabBar.context, R.color.white))
        tabBar.setTabTextColors(ContextCompat.getColor(tabBar.context, R.color.white40)
                , ContextCompat.getColor(tabBar.context, R.color.white))
        tabBar.setupWithViewPager(viewPager)
    }

    private fun setTopBar() {
        topBar.setText("Market")
        topBar.setChatNoti("0")
        topBar.setNoti("0")
        topBar.haveSearch(true)
        topBar.getSearchHolder()?.setOnClickListener {
            var bundle = Bundle().also { bundle ->
                bundle.putParcelable("model",SearchModel())
                bundle.putInt("tag",1)
            }
            startActivity(Intent(context,FilterActivity::class.java)
                    .putExtras(bundle))
        }

        topBar.getChatHolder()?.setOnClickListener {
            startActivity(Intent(context,ChatListActivity::class.java))
        }
    }

}
