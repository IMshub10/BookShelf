package com.summer.bookshelf.persistence.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    @ColumnInfo("email")
    var email: String = "",
    @ColumnInfo("name")
    var name: String = "",
    @ColumnInfo("password")
    var password: String = "",
    @ColumnInfo("country")
    var country: String = ""
)