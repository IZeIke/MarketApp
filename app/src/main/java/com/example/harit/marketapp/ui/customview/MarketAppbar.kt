package com.example.harit.marketapp.ui.customview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.RelativeLayout
import com.example.harit.marketapp.R
import kotlinx.android.synthetic.main.view_appbar.view.*

class MarketAppbar @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    init {
        LayoutInflater.from(context).inflate(R.layout.view_appbar,this)
    }

    fun getSearchHolder(): RelativeLayout? {
        return searchHolder
    }

    fun getFilterHolder(): RelativeLayout? {
        return filterHolder
    }

    fun getChatHolder(): RelativeLayout? {
        return chatHolder
    }

    fun setText(text : String){
        textView.text = text
    }

    fun haveSearch(boolean: Boolean){
        if(boolean){
            searchHolder.visibility = View.VISIBLE
        }else{
            searchHolder.visibility = View.GONE
        }
    }

    fun haveFilter(boolean: Boolean){
        if(boolean){
            filterHolder.visibility = View.VISIBLE
        }
    }

    fun haveChat(boolean: Boolean){
        if (!boolean){
            chatIc.visibility = View.GONE
            chatNoti.visibility = View.GONE
        }
    }

    fun haveBack(boolean: Boolean){
        if(boolean){
            backHolder.visibility = View.VISIBLE
            backHolder.visibility = View.VISIBLE
        }
    }

    fun haveNoti(boolean: Boolean)
    {
        if(!boolean){
            notiIc.visibility = View.GONE
            chatIc.visibility = View.GONE
            notiNoti.visibility = View.GONE
            chatNoti.visibility = View.GONE
        }
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
