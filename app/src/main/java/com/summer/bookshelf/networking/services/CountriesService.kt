package com.summer.bookshelf.networking.services

import com.summer.bookshelf.networking.APIConstants.COUNTRIES
import com.summer.bookshelf.networking.beans.CountriesResponse
import retrofit2.Response
import retrofit2.http.GET

interface CountriesService {

    @GET(COUNTRIES)
    suspend fun fetchCountries(): Response<CountriesResponse>
}