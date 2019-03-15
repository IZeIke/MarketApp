package com.example.harit.marketapp.ui.searchPage

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.harit.marketapp.R
import com.example.harit.marketapp.ui.model.SearchModel
import kotlinx.android.synthetic.main.activity_filter.*

class FilterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filter)

        setTopBar()
        setButton()

        summitButton.setOnClickListener {
            startActivity(Intent(this,FeedActivity::class.java).putExtra("searchModel",SearchModel()))
            finish()
        }
    }

    private fun setButton() {
        setButton.isSelected = true
        memberButton.isSelected = true
        lastedButton.isSelected = true
        setNumberButton.isSelected = true
        formatAllButton.isSelected = true

        setButton.setOnClickListener {
            setButton.isSelected = true
            singleButton.isSelected = false
        }

        singleButton.setOnClickListener {
            setButton.isSelected = false
            singleButton.isSelected = true
        }

        memberButton.setOnClickListener {
            memberButton.isSelected = true
            memberChooseButton.isSelected = false
        }

        memberChooseButton.setOnClickListener {
            memberButton.isSelected = false
            memberChooseButton.isSelected = true
        }

        lastedButton.setOnClickListener {
            lastedButton.isSelected = true
            priceButton1.isSelected = false
            priceButton2.isSelected = false
        }

        priceButton1.setOnClickListener {
            priceButton1.isSelected = true
            lastedButton.isSelected = false
            priceButton2.isSelected = false
        }

        priceButton2.setOnClickListener {
            priceButton2.isSelected = true
            lastedButton.isSelected = false
            priceButton1.isSelected = false
        }

        setNumberButton.setOnClickListener {
            setNumberButton.isSelected = true
            setNumberChooseButton.isSelected = false
        }

        setNumberChooseButton.setOnClickListener {
            setNumberButton.isSelected = false
            setNumberChooseButton.isSelected = true
        }

        formatAllButton.setOnClickListener {
            formatAllButton.isSelected = true
            compButton.isSelected = false
            fullButton.isSelected = false
            halfButton.isSelected = false
            closeUpButton.isSelected = false
        }

        compButton.setOnClickListener {
            formatAllButton.isSelected = false
            compButton.isSelected = true
            fullButton.isSelected = false
            halfButton.isSelected = false
            closeUpButton.isSelected = false
        }

        fullButton.setOnClickListener {
            formatAllButton.isSelected = false
            compButton.isSelected = false
            fullButton.isSelected = true
            halfButton.isSelected = false
            closeUpButton.isSelected = false
        }

        halfButton.setOnClickListener {
            formatAllButton.isSelected = false
            compButton.isSelected = false
            fullButton.isSelected = false
            halfButton.isSelected = true
            closeUpButton.isSelected = false
        }

        closeUpButton.setOnClickListener {
            formatAllButton.isSelected = false
            compButton.isSelected = false
            fullButton.isSelected = false
            halfButton.isSelected = false
            closeUpButton.isSelected = true
        }
    }

    private fun setTopBar() {
        topBar?.let {
            it.haveSearch(false)
            it.setText("Search")
            it.haveNoti(false)
        }

    }


}