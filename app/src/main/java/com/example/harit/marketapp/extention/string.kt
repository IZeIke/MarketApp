package com.example.harit.marketapp.extention

import android.content.Context
import android.widget.Toast

fun String.Toast(context: Context){
    Toast.makeText(context,this,Toast.LENGTH_SHORT).show()
}