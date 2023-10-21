package com.summer.bookshelf.networking.services

import com.summer.bookshelf.networking.APIConstants.BOOK_LIST
import com.summer.bookshelf.networking.beans.BookBean
import retrofit2.Response
import retrofit2.http.GET

interface BookService {
    @GET(BOOK_LIST)
    suspend fun fetchBookList(): Response<List<BookBean>>
}