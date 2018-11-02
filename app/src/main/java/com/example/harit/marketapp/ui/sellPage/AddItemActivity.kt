package com.example.harit.marketapp.ui.sellPage

import android.annotation.TargetApi
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import com.example.harit.marketapp.R
import com.google.android.material.chip.Chip
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_additem.*

class AddItemActivity : AppCompatActivity() {



    @TargetApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_additem)

        stepperLayout.adapter = SellPageAdapter(supportFragmentManager,this)

        /*FirebaseFirestore.getInstance().collection("NameList").get().addOnCompleteListener {
            if (it.isSuccessful) {
                it.result.forEach {
                    var chip = Chip(nameGroup.context)
                    chip.isCheckable = true
                    chip.checkedIcon = null
                    chip.chipBackgroundColor = getColorStateList(R.color.bg_chip_state_list)
                    chip.text = it.get("name").toString()
                    nameGroup.addView(chip)
                }
            }
        }*/
    }
}
