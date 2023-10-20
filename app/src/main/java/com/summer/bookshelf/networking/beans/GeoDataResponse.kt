package com.summer.bookshelf.networking.beans


import com.google.gson.annotations.SerializedName

data class GeoDataResponse(
    @SerializedName("status")
    val status: String?,
    @SerializedName("country")
    val country: String?,
    @SerializedName("countryCode")
    val countryCode: String?
)