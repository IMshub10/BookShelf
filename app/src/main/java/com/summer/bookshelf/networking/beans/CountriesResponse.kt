package com.summer.bookshelf.networking.beans


import com.google.gson.annotations.SerializedName
import org.json.JSONObject

data class CountriesResponse(
    @SerializedName("access")
    val access: String?,
    @SerializedName("data")
    val data: JSONObject?,
    @SerializedName("status")
    val status: String?,
    @SerializedName("status-code")
    val statusCode: Int?,
    @SerializedName("version")
    val version: String?
)