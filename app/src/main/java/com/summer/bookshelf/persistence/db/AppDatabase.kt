package com.summer.bookshelf.persistence.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.summer.bookshelf.persistence.db.daos.AppDao
import com.summer.bookshelf.persistence.db.entities.CountryEntity
import com.summer.bookshelf.persistence.db.entities.UserEntity


@Database(
    entities = [CountryEntity::class, UserEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun appDao(): AppDao

    companion object {
        @Volatile
        private var instance: AppDatabase? = null
        private val LOCK = Any()
        private const val DB_NAME = "book_shelf"

        operator fun invoke(context: Context) = instance
            ?: synchronized(LOCK) {
                instance
                    ?: Room.databaseBuilder(
                        context.applicationContext, AppDatabase::class.java, DB_NAME
                    )
                        .build().also {
                            instance = it
                        }
            }
    }
}