package com.summer.bookshelf.networking.beans


import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName

data class CountriesResponse(
    @SerializedName("access")
    val access: String?,
    @SerializedName("data")
    val data: JsonObject,
    @SerializedName("status")
    val status: String?,
    @SerializedName("status-code")
    val statusCode: Int?,
    @SerializedName("version")
    val version: String?
)