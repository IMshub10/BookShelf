package com.summer.bookshelf.repositories

import com.summer.bookshelf.persistence.db.entities.UserEntity
import com.summer.bookshelf.ui.models.DropdownModel
import kotlinx.coroutines.flow.Flow

interface LoginRepository {

    fun fetchCountryList(): Flow<List<DropdownModel>>

    fun fetchDefaultCountry(): Flow<DropdownModel?>

    suspend fun insertUser(userEntity: UserEntity)

    suspend fun getUserByCredentials(email: String, password: String?): Int

    suspend fun setLoggedInUserId(userId: Int)

    suspend fun checkUserExists(email: String): Boolean
}