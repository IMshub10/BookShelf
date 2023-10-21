package com.summer.bookshelf.ui.screens.main

import android.util.Log
import androidx.lifecycle.ViewModel
import com.summer.bookshelf.networking.services.BookService
import com.summer.bookshelf.networking.services.CountriesService
import com.summer.bookshelf.networking.services.IPService

class MainViewModel(
    private val bookService: BookService,
    private val countriesService: CountriesService,
    private val ipService: IPService
) : ViewModel() {

    companion object {
        const val TAG = "MainViewModel"
    }

    suspend fun fetchBooks() {
        val books = bookService.fetchBookList()
        Log.d(TAG, books.body().toString())
    }

    suspend fun fetchCountries() {
        val countries = countriesService.fetchCountries()
        Log.d(TAG, countries.body().toString())
    }

    suspend fun fetchMyGeoData() {
        val geoData = ipService.fetchMyGeoData()
        Log.d(TAG, geoData.body().toString())
    }
}