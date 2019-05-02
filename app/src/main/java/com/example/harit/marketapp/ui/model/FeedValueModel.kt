package com.example.harit.marketapp.ui.model

import android.os.Parcelable
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.android.parcel.Parcelize

data class FeedValueModel(
        var feedList: MutableList<FeedModel>? = null,
        var lastVisible : DocumentSnapshot? = null
)