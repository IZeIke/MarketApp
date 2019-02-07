package com.example.harit.marketapp.ui.feedPage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.asksira.bsimagepicker.GridItemSpacingDecoration
import com.example.harit.marketapp.R
import com.example.harit.marketapp.ui.adapter.FeedPageAdapter
import kotlinx.android.synthetic.main.fragment_feed.*

class FeedFragment : Fragment() {

    companion object {
        fun newInstance(): FeedFragment{
            val fragment = FeedFragment()
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return LayoutInflater.from(context).inflate(R.layout.fragment_feed,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        feedRecyclerView.layoutManager = GridLayoutManager(context,2)
        feedRecyclerView.addItemDecoration(GridItemSpacingDecoration(2,20,true))
        feedRecyclerView.adapter = FeedPageAdapter(context!!)

    }
}