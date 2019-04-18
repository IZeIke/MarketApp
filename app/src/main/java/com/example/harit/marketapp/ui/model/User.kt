package com.example.harit.marketapp.ui.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(var email : String? = null,
                var id : String? = null,
                var name : String? = null,
                var username : String? = null,
                var nid : Int? = null,
                var imageUrl : String? = null
                ) : Parcelable