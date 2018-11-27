package com.example.harit.marketapp.ui.model

import android.accounts.AuthenticatorDescription
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ServerTimestamp
import com.robertlevonyan.components.picker.Str

data class FeedItem(
        @ServerTimestamp val create : Timestamp? = null,
        var imageUrl : HashMap<Int,String>? = null,
        var name : String? = null,
        var price : Int? = null,
        var filter : HashMap<String,String>? = null,
        var status : String? = null,
        var description: String? = null,
        var user : HashMap<String,String>? = null,
        var shipping : String? = null,
        var payment : String? = null
)