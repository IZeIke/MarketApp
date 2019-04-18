package com.example.harit.marketapp.ui.model

import android.accounts.AuthenticatorDescription
import android.os.Parcelable
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ServerTimestamp
import com.robertlevonyan.components.picker.Str
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue
import java.util.*

data class FeedItem(
        @ServerTimestamp val create : Date? = null,
        var imageUrl : ArrayList<String>? = null,
        var name : String? = null,
        var price : Int? = null,
        var filter : HashMap<String,String>? = null,
        var status : String? = null,
        var description: String? = null,
        var user : HashMap<String,Any>? = null,
        var shipping : String? = null,
        var payment : String? = null,
        var id : String? = null
)