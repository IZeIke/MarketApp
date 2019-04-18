package com.example.harit.marketapp.ui.searchPage

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.harit.marketapp.R
import kotlinx.android.synthetic.main.activity_filter.*
import com.example.harit.marketapp.ui.model.*
import com.google.firebase.firestore.FirebaseFirestore
import ir.mirrajabi.searchdialog.core.SearchResultListener
import ir.mirrajabi.searchdialog.SimpleSearchDialogCompat
import biz.kasual.materialnumberpicker.MaterialNumberPicker
import android.graphics.Color
import android.view.View
import com.example.harit.marketapp.ui.model.Set


class FilterActivity : AppCompatActivity() {

    lateinit var bundle : Bundle
    lateinit var model : SearchModel
    private var numberPicker : MaterialNumberPicker? = null
    var list : FilterList? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filter)

        bundle = intent.extras
        model = bundle.getParcelable("model")
        setTopBar()
        setButton()

        getFilterData()

        summitButton.setOnClickListener {

            bundle.getInt("tag")?.let { tag ->
                if (tag == 1){
                    startActivity(Intent(this,FeedActivity::class.java).putExtra("searchModel",model))
                    finish()
                }else{
                    setResult(Activity.RESULT_OK,Intent().putExtra("searchModel",model))
                    finish()
                }
            }

        }
    }

    override fun onBackPressed() {
        setResult(Activity.RESULT_CANCELED, Intent())
        finish()
    }

    private fun getPicker() : MaterialNumberPicker {

        var maxValue = if(setButton.isSelected){
            list?.set?.get("normal")!!
        }else{
            list?.set?.get("single")!!
        }

        return MaterialNumberPicker.Builder(this)
                .minValue(1)
                .maxValue(maxValue)
                .defaultValue(1)
                .backgroundColor(Color.WHITE)
                .textColor(Color.BLACK)
                .textSize(20f)
                .enableFocusability(false)
                .wrapSelectorWheel(true)
                .build()
    }

    private fun getFilterData() {
        val db = FirebaseFirestore.getInstance()

        val docRef = db.collection("Filter").document("filterList")
        docRef.get().addOnSuccessListener { documentSnapshot ->
            list = documentSnapshot.toObject(FilterList::class.java)
        }

    }

    private fun setButton() {

        when(model?.format){
            Format.SET -> setButton.isSelected = true
            Format.SINGLE -> {
                singleButton.isSelected = true
                formatHolder.visibility = View.GONE
            }
            else -> {}
        }

        when(model?.set){
            0 -> setNumberAllButton.isSelected = true
            else -> {
                setNumberChooseButton.text = model?.set.toString()
                setNumberChooseButton.isSelected = true
            }
        }

        when(model?.member){
            "All" -> memberButton.isSelected = true
            else -> {
                memberChooseButton.text = model?.member
                memberChooseButton.isSelected = true
            }
        }

        when(model?.sort){
            Sort.LASTED -> lastedButton.isSelected = true
            Sort.PRICEMORE -> priceButton1.isSelected = true
            Sort.PRICELESS -> priceButton2.isSelected = true
            else ->{}
        }

        when(model?.type){
            Type.ALL -> AllButton.isSelected = true
            Type.CLOSEUP -> closeUpButton.isSelected = true
            Type.COMP -> compButton.isSelected = true
            Type.FULL -> fullButton.isSelected = true
            Type.HALF -> halfButton.isSelected = true
            else ->{}
        }


        setButton.setOnClickListener {
            formatHolder.visibility = View.VISIBLE
            model.format = Format.SET
            setButton.isSelected = true
            singleButton.isSelected = false
        }

        singleButton.setOnClickListener {
            formatHolder.visibility = View.GONE
            model.format = Format.SINGLE
            setButton.isSelected = false
            singleButton.isSelected = true
        }

        memberButton.setOnClickListener {
            model.member = "All"
            memberButton.isSelected = true
            memberChooseButton.isSelected = false
            memberChooseButton.text = "เลือกเมมเบอร์"
        }

        memberChooseButton.setOnClickListener {

            SimpleSearchDialogCompat(this, "Search...",
                    "What are you looking for...?", null, createSampleData(list?.nameList),
                    SearchResultListener<SampleSearchModel> { dialog, item, position ->
                        memberChooseButton.text = item.title
                        model.member = item.title!!
                        memberButton.isSelected = false
                        memberChooseButton.isSelected = true

                        dialog.dismiss()
                    }).show()

        }

        lastedButton.setOnClickListener {
            model.sort = Sort.LASTED
            lastedButton.isSelected = true
            priceButton1.isSelected = false
            priceButton2.isSelected = false
        }

        priceButton1.setOnClickListener {
            model.sort = Sort.PRICEMORE
            priceButton1.isSelected = true
            lastedButton.isSelected = false
            priceButton2.isSelected = false
        }

        priceButton2.setOnClickListener {
            model.sort = Sort.PRICELESS
            priceButton2.isSelected = true
            lastedButton.isSelected = false
            priceButton1.isSelected = false
        }

        setNumberAllButton.setOnClickListener {
            model.set = 0
            setNumberAllButton.isSelected = true
            setNumberChooseButton.isSelected = false
            setNumberChooseButton.text = "เลือกเซ็ท"
        }

        setNumberChooseButton.setOnClickListener {

            numberPicker = getPicker()

            AlertDialog.Builder(this,R.style.MyDialogTheme)
                    .setTitle("เลือกเซ็ท")
                    .setView(numberPicker)
                    .setPositiveButton("ok") { dialog, which ->
                        setNumberChooseButton.text = "set ${numberPicker?.value}"
                        model.set = numberPicker?.value!!
                        setNumberAllButton.isSelected = false
                        setNumberChooseButton.isSelected = true
                    }
                    .show()
        }

        AllButton.setOnClickListener {
            model.type = Type.ALL
            AllButton.isSelected = true
            compButton.isSelected = false
            fullButton.isSelected = false
            halfButton.isSelected = false
            closeUpButton.isSelected = false
        }

        compButton.setOnClickListener {
            model.type = Type.COMP
            AllButton.isSelected = false
            compButton.isSelected = true
            fullButton.isSelected = false
            halfButton.isSelected = false
            closeUpButton.isSelected = false
        }

        fullButton.setOnClickListener {
            model.type = Type.FULL
            AllButton.isSelected = false
            compButton.isSelected = false
            fullButton.isSelected = true
            halfButton.isSelected = false
            closeUpButton.isSelected = false
        }

        halfButton.setOnClickListener {
            model.type = Type.HALF
            AllButton.isSelected = false
            compButton.isSelected = false
            fullButton.isSelected = false
            halfButton.isSelected = true
            closeUpButton.isSelected = false
        }

        closeUpButton.setOnClickListener {
            model.type = Type.CLOSEUP
            AllButton.isSelected = false
            compButton.isSelected = false
            fullButton.isSelected = false
            halfButton.isSelected = false
            closeUpButton.isSelected = true
        }
    }

    private fun setTopBar() {
        topBar?.let {
            it.haveSearch(false)
            it.setText("Filter")
            it.haveNoti(false)
            it.haveBack(true)
            it.getBackHolder()?.setOnClickListener {
                onBackPressed()
            }
        }

    }

    private fun createSampleData(nameList: Map<String, Int>?): ArrayList<SampleSearchModel> {
        val items = arrayListOf<SampleSearchModel>()
        nameList?.let {
            for ((key, _) in it)
                items.add(SampleSearchModel(key))
        }
        return items
    }

}