package com.summer.bookshelf.repositories

import com.summer.bookshelf.networking.services.CountriesService
import com.summer.bookshelf.networking.services.IPService
import com.summer.bookshelf.persistence.db.daos.AppDao
import com.summer.bookshelf.persistence.db.entities.CountryEntity
import com.summer.bookshelf.persistence.db.entities.UserEntity
import com.summer.bookshelf.ui.models.DropdownModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class SignUpRepositoryImpl(
    private val countriesService: CountriesService,
    private val ipService: IPService,
    private val appDao: AppDao
) : SignUpRepository {

    override fun fetchCountryList() = flow {
        try {
            val localCountries = appDao.fetchCountries() //API call

            if (localCountries.isEmpty()) {

                val remoteCountries = countriesService.fetchCountries()
                val resultCountries = mutableListOf<DropdownModel>()

                remoteCountries.body()!!.data.let {
                    it.keySet().forEach { key ->

                        resultCountries.add(
                            DropdownModel(
                                id = key, title = it[key].asJsonObject["country"].asString
                            )
                        )

                        appDao.insertIgnoreCountry(
                            CountryEntity(
                                countryCode = key,
                                name = it[key].asJsonObject["country"].asString
                            )
                        )

                    }
                }
                emit(resultCountries)

            } else {
                emit(localCountries.map {
                    DropdownModel(
                        id = it.countryCode, title =
                        it.name
                    )
                })

            }
        } catch (e: Exception) {
            e.printStackTrace()
            emit(listOf())
        }
    }.flowOn(
        Dispatchers.IO
    )

    override fun fetchDefaultCountry() = flow {
        try {
            val defaultCountry = ipService.fetchMyGeoData()
            defaultCountry.body()?.let { response ->
                response.country?.let { country ->
                    response.countryCode?.let { countryCode ->
                        emit(DropdownModel(id = countryCode, title = country))
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emit(null)
        }
    }

    override suspend fun insertUser(userEntity: UserEntity)  = appDao.insertUser(userEntity)




}