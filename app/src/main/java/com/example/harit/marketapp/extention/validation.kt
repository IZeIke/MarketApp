package com.example.harit.marketapp.extention

import android.util.Patterns



fun String.isEmailValid() : Boolean {
    val pattern = Patterns.EMAIL_ADDRESS
    val matcher = pattern.matcher(this)
    return matcher.matches()
}

fun String.isPasswordValid() : Boolean {
    return this.length >= 6
}

