package com.example.harit.marketapp.ui.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ScoreModel(
        var scoreSum : Double? = null,
        var number : Double? = null
) : Parcelable