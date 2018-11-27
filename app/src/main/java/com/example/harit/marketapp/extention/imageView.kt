package com.example.harit.marketapp.extention

import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide
import java.io.File

fun ImageView.setImageUri(uri: Uri){
    Glide.with(this.context)
            .load(File(uri.path)) // Uri of the picture
            .into(this)
}