package com.example.harit.marketapp.ui.sellPage

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.harit.marketapp.R
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_sell.*

class SellFragment : Fragment() {

    companion object {
        fun newInstance() : SellFragment{
            val fragment = SellFragment()
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return LayoutInflater.from(context).inflate(R.layout.fragment_sell,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fab.setOnClickListener { view ->
            startActivity(Intent(activity,AddItemActivity::class.java))
        }
    }

}