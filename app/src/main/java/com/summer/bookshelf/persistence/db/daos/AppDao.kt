package com.summer.bookshelf.persistence.db.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.summer.bookshelf.persistence.db.entities.CountryEntity
import com.summer.bookshelf.persistence.db.entities.UserEntity

@Dao
interface AppDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertIgnoreCountry(countryEntity: CountryEntity)

    @Query("Select * from countries order by name")
    fun fetchCountries(): List<CountryEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertUser(userEntity: UserEntity)

}