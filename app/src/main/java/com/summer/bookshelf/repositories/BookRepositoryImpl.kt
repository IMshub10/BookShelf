package com.summer.bookshelf.repositories

import com.summer.bookshelf.networking.services.BookService
import com.summer.bookshelf.persistence.db.daos.AppDao
import com.summer.bookshelf.persistence.db.entities.BookEntity
import com.summer.bookshelf.persistence.db.entities.UserBookMetaData
import com.summer.bookshelf.persistence.pref.Preference
import com.summer.bookshelf.ui.models.BookModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.util.Calendar

class BookRepositoryImpl(
    private val bookService: BookService,
    private val appDao: AppDao,
    private val preference: Preference
) : BookRepository {

    override fun fetchBooks(query: String) = flow {
        try {
            val localBooks = appDao.getAllBooks(preference.getLoggedInUserId())
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
                            isBookmarked = false
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
                emit(resultList.filter {
                    it.title.lowercase().contains(query.lowercase())
                }.groupBy {
                    it.year
                }.toSortedMap(comparator = { o1, o2 ->
                    o1 - o2
                }))
            } else {
                emit(localBooks.filter {
                    it.title.lowercase().contains(query.lowercase())
                }.groupBy {
                    it.year
                }.toSortedMap(comparator = { o1, o2 ->
                    o1 - o2
                }))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emit(hashMapOf<Int, List<BookModel>>())
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun bookMark(bookModel: BookModel) {
        val userId = preference.getLoggedInUserId()
        val bookMetaData = appDao.getBookMetaDataByIds(
            bookId = bookModel.id,
            userId = userId
        )
        if (bookMetaData == null) {
            appDao.insertIgnoreBookMeta(
                UserBookMetaData(
                    userId = userId,
                    bookId = bookModel.id,
                    isBookmarked = bookModel.isBookmarked,
                    tags = emptyList()
                )
            )
        } else {
            appDao.updateBookMark(
                userId = userId,
                bookId = bookModel.id,
                isBookmarked = bookModel.isBookmarked
            )
        }
    }

    override suspend fun logout() = preference.logout()

    override suspend fun addTag(bookId: String, tagName: String) {
        val userId = preference.getLoggedInUserId()
        val bookMetaData = appDao.getBookMetaDataByIds(
            bookId = bookId,
            userId = userId
        )
        if (bookMetaData == null) {
            appDao.insertIgnoreBookMeta(
                UserBookMetaData(
                    userId = userId,
                    bookId = bookId,
                    isBookmarked = false,
                    tags = listOf(tagName)
                )
            )
        } else {
            appDao.updateTags(
                userId = userId,
                bookId = bookId,
                tags = bookMetaData.tags.toMutableList().apply {
                    add(tagName)
                }
            )
        }
    }

    override suspend fun fetchUserMetaData(bookId: String) =
        appDao.fetchTags(userId = preference.getLoggedInUserId(), bookId = bookId)
}