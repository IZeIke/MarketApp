package com.example.harit.marketapp.ui.model

import android.os.Parcelable
import com.google.firebase.firestore.ServerTimestamp
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class FeedModel(
        @ServerTimestamp val create : Date? = null,
        var imageUrl : ArrayList<String>? = null,
        var name : String? = null,
        var price : Int? = null,
        var filter : HashMap<String,String>? = null,
        var status : String? = null,
        var description: String? = null,
        var user : User? = null,
        var shipping : String? = null,
        var payment : String? = null
) : Parcelable