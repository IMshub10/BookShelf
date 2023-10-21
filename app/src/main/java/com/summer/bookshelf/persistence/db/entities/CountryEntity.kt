package com.summer.bookshelf.persistence.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "countries")
data class CountryEntity(
    @PrimaryKey(false)
    @ColumnInfo("country_code")
    var countryCode: String,

    @ColumnInfo("name")
    var name: String,
)