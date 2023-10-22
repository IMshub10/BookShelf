package com.summer.bookshelf.persistence.db.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.summer.bookshelf.persistence.db.entities.BookEntity
import com.summer.bookshelf.persistence.db.entities.CountryEntity
import com.summer.bookshelf.persistence.db.entities.UserBookMetaData
import com.summer.bookshelf.persistence.db.entities.UserEntity
import com.summer.bookshelf.ui.models.BookModel

@Dao
interface AppDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertIgnoreCountry(countryEntity: CountryEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertIgnoreBook(bookEntity: BookEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertUser(userEntity: UserEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertIgnoreBookMeta(userBookMetaData: UserBookMetaData)

    @Query("Select * from countries order by name")
    fun fetchCountries(): List<CountryEntity>

    @Query("Select id from users where email = :email and password = :password")
    fun getUserByCredentials(email: String, password: String): Int?

    @Query("Select id from users where email = :email")
    fun getUserByEmail(email: String): Int?

    @Query(
        " Select id, title, image, score, published_chapter_year as year, coalesce(user_books_meta_data.is_bookmarked, 0) as is_bookmarked  from books " +
                " left join user_books_meta_data on user_books_meta_data.user_id = :userId and user_books_meta_data.book_id = books.id  " +
                " order by published_chapter_year desc, title "
    )
    fun getAllBooks(userId: Int): List<BookModel>

    @Query("Select count(1) from users where email = :email")
    fun checkUserExists(email: String): Boolean

    @Query("Select * from user_books_meta_data where book_id=:bookId and user_id =:userId")
    fun getBookMetaDataByIds(bookId: String, userId: Int) : UserBookMetaData?

    @Query("Update user_books_meta_data set is_bookmarked = :isBookmarked where book_id =:bookId and user_id =:userId")
    fun updateBookMark(userId: Int, bookId: String, isBookmarked: Boolean)

}