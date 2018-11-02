package com.example.harit.marketapp.ui.customview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.example.harit.marketapp.R
import kotlinx.android.synthetic.main.view_appbar.view.*

class MarketAppbar @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    init {
        LayoutInflater.from(context).inflate(R.layout.view_appbar,this)
    }

    fun setText(text : String){
        textView.text = text
    }

    fun setNoti(text : String){
        if(text == "0"){
            notiNoti.visibility = View.GONE
        }else{
            notiNoti.visibility = View.VISIBLE
            notiNoti.text = text
        }
    }

    fun setChatNoti(text : String){
        if(text == "0") {
            chatNoti.visibility = View.GONE
        }else{
            chatNoti.text = text
            chatNoti.visibility = View.VISIBLE
        }
    }

}