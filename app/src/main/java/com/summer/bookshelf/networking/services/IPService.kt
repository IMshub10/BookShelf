package com.summer.bookshelf.networking.services

import com.summer.bookshelf.networking.APIConstants.MY_GEO_DATA
import com.summer.bookshelf.networking.beans.GeoDataResponse
import retrofit2.Response
import retrofit2.http.GET

interface IPService {
    @GET(MY_GEO_DATA)
    suspend fun fetchMyGeoData(): Response<GeoDataResponse?>
}