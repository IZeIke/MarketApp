package com.example.harit.marketapp.ui.model

import com.google.firebase.firestore.ServerTimestamp
import java.util.*

data class NotiModel(
        @ServerTimestamp val create : Date? = null,
        var message : String? = null,
        var user : User? = null,
        var id : String? = null
)