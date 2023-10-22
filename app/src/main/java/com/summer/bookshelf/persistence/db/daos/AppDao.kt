package com.summer.bookshelf.persistence.db.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.summer.bookshelf.persistence.db.entities.BookEntity
import com.summer.bookshelf.persistence.db.entities.CountryEntity
import com.summer.bookshelf.persistence.db.entities.UserEntity

@Dao
interface AppDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertIgnoreCountry(countryEntity: CountryEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertIgnoreBook(bookEntity: BookEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertUser(userEntity: UserEntity)

    @Query("Select * from countries order by name")
    fun fetchCountries(): List<CountryEntity>

    @Query("Select id from users where email = :email and password = :password")
    fun getUserByCredentials(email: String, password: String) : Int?

    @Query("Select id from users where email = :email")
    fun getUserByEmail(email: String) : Int?

    @Query("Select * from books order by published_chapter_year desc, title ")
    fun getAllBooks(): List<BookEntity>

    @Query("Select count(1) from users where email = :email")
    fun checkUserExists(email: String): Boolean

}