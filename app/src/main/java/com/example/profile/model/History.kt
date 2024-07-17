package com.example.profile.model

import com.google.gson.annotations.SerializedName

class History {
    var title : String? = null
    @SerializedName("is_up")
    var isUp : Boolean? = null
}