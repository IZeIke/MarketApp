package com.example.harit.marketapp.ui.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SearchModel(
        var format: String = Format.SET,
        var member: String = "All",
        var sort: String = Sort.LASTED,
        var set: Int = 0,
        var type: String = Type.ALL
) : Parcelable