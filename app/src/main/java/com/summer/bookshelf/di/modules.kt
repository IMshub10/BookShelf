package com.summer.bookshelf.di

import android.app.Application
import com.summer.bookshelf.networking.remotes.BookServiceMaker
import com.summer.bookshelf.networking.remotes.CountriesServiceMaker
import com.summer.bookshelf.networking.remotes.IPServiceMaker
import com.summer.bookshelf.networking.services.CountriesService
import com.summer.bookshelf.networking.services.IPService
import com.summer.bookshelf.persistence.db.AppDatabase
import com.summer.bookshelf.persistence.db.daos.AppDao
import com.summer.bookshelf.persistence.pref.Preference
import com.summer.bookshelf.repositories.SignUpRepository
import com.summer.bookshelf.repositories.SignUpRepositoryImpl
import com.summer.bookshelf.ui.screens.main.MainViewModel
import com.summer.bookshelf.ui.screens.user.frags.RegisterViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val localDataSourceModule = module {

    fun providePreference(application: Application) =
        Preference(Preference.createSharedPref(application))

    fun provideDatabase(
        application: Application
    ) = AppDatabase(context = application)

    fun provideAppDao(appDatabase: AppDatabase) = appDatabase.appDao()

    single { providePreference(application = androidApplication()) }
    single { provideDatabase(application = androidApplication()) }
    single { provideAppDao(appDatabase = get()) }
}

val remoteModule = module {
    fun provideBookRemoteModule() = BookServiceMaker()

    fun provideCountriesRemoteModule() = CountriesServiceMaker()

    fun provideIPRemoteModule() = IPServiceMaker()

    single { provideBookRemoteModule() }
    single { provideCountriesRemoteModule() }
    single { provideIPRemoteModule() }
}

val repositoryModule = module {
    fun provideSignUpRepository(
        countriesService: CountriesService, ipService: IPService, appDao: AppDao
    ): SignUpRepository = SignUpRepositoryImpl(
        countriesService = countriesService, ipService = ipService, appDao = appDao
    )

    single {
        provideSignUpRepository(
            countriesService = get(), ipService = get(), appDao = get()
        )
    }
}

val viewModelModule = module {
    viewModel {
        RegisterViewModel(signUpRepository = get())
    }
    viewModel {
        MainViewModel(bookService = get(), countriesService = get(), ipService = get())
    }
}