package com.example.profile.model

import com.google.gson.annotations.SerializedName

class DataResponse {
    var success : Boolean? = null
    var message : String? = null
    @SerializedName("full_name")
    var fullName : String? = null
    var position : String? = null
    @SerializedName("history")
    var listHistory : List<History>? = null
}