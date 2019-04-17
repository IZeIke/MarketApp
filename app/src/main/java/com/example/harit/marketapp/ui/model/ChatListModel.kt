package com.example.harit.marketapp.ui.model

import android.os.Parcelable
import com.google.firebase.firestore.ServerTimestamp
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class ChatListModel(
        var user : User? = null,
        @ServerTimestamp var create : Date? = null,
        var message : String? = null,
        var messageType : String? = null
) : Parcelable