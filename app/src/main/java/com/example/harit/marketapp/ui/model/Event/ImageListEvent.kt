package com.example.harit.marketapp.ui.model.Event

import android.net.Uri
import com.example.harit.marketapp.ui.model.ChipList

class ImageListEvent {

    var imageList: MutableList<Uri>? = null


    constructor(imageList: MutableList<Uri>) {
        this.imageList = imageList
    }
}