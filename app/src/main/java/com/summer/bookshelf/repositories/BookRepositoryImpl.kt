package com.summer.bookshelf.repositories

import com.summer.bookshelf.networking.services.BookService
import com.summer.bookshelf.persistence.db.daos.AppDao
import com.summer.bookshelf.persistence.db.entities.BookEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.util.Calendar

class BookRepositoryImpl(
    private val bookService: BookService,
    private val appDao: AppDao
) : BookRepository {

    override fun fetchBooks() = flow {
        try {
            val localBooks = appDao.getAllBooks()
            if (localBooks.isEmpty()) {
                val remoteBooks = bookService.fetchBookList()
                val resultList = mutableListOf<BookEntity>()
                val calendar = Calendar.getInstance()

                loop@ for (bean in remoteBooks.body()!!) {

                    if (bean.title == null || bean.id == null || bean.publishedChapterDate == null || bean.score == null)
                        continue@loop

                    calendar.timeInMillis = bean.publishedChapterDate * 1000

                    BookEntity(
                        id = bean.id,
                        publishedChapterDate = bean.publishedChapterDate,
                        image = bean.image,
                        publishedChapterYear = calendar[Calendar.YEAR],
                        score = bean.score,
                        title = bean.title
                    ).run {
                        resultList.add(this)
                        appDao.insertIgnoreBook(this)
                    }
                }
                emit(resultList)
            } else {
                emit(localBooks)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emit(listOf())
        }
    }.flowOn(Dispatchers.IO)

}