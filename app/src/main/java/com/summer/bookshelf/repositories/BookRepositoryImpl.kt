package com.summer.bookshelf.repositories

import com.summer.bookshelf.networking.services.BookService
import com.summer.bookshelf.persistence.db.daos.AppDao
import com.summer.bookshelf.persistence.db.entities.BookEntity
import com.summer.bookshelf.ui.models.BookModel
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
                val resultList = mutableListOf<BookModel>()
                val calendar = Calendar.getInstance()

                loop@ for (bean in remoteBooks.body()!!) {

                    if (bean.title == null || bean.id == null || bean.publishedChapterDate == null || bean.score == null)
                        continue@loop

                    calendar.timeInMillis = bean.publishedChapterDate * 1000

                    resultList.add(
                        BookModel(
                            id = bean.id,
                            image = bean.image,
                            title = bean.title,
                            score = bean.score.toString(),
                            year = calendar[Calendar.YEAR],
                        )
                    )

                    appDao.insertIgnoreBook(
                        BookEntity(
                            id = bean.id,
                            publishedChapterDate = bean.publishedChapterDate,
                            image = bean.image,
                            publishedChapterYear = calendar[Calendar.YEAR],
                            score = bean.score,
                            title = bean.title
                        )
                    )
                }
                emit(resultList.groupBy {
                    it.year
                }.toSortedMap())
            } else {
                emit(localBooks.map {
                    BookModel(
                        id = it.id,
                        image = it.image,
                        title = it.title,
                        score = it.score.toString(),
                        year = it.publishedChapterYear
                    )
                }.groupBy {
                    it.year
                }.toSortedMap())
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emit(hashMapOf<Int, List<BookModel>>())
        }
    }.flowOn(Dispatchers.IO)

}