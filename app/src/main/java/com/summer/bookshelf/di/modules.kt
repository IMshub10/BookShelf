package com.summer.bookshelf.di

import android.app.Application
import com.summer.bookshelf.networking.remotes.BookServiceMaker
import com.summer.bookshelf.networking.remotes.CountriesServiceMaker
import com.summer.bookshelf.networking.remotes.IPServiceMaker
import com.summer.bookshelf.networking.services.BookService
import com.summer.bookshelf.networking.services.CountriesService
import com.summer.bookshelf.networking.services.IPService
import com.summer.bookshelf.persistence.db.AppDatabase
import com.summer.bookshelf.persistence.db.daos.AppDao
import com.summer.bookshelf.persistence.pref.Preference
import com.summer.bookshelf.repositories.BookRepository
import com.summer.bookshelf.repositories.BookRepositoryImpl
import com.summer.bookshelf.repositories.LoginRepository
import com.summer.bookshelf.repositories.LoginRepositoryImpl
import com.summer.bookshelf.repositories.SplashRepository
import com.summer.bookshelf.repositories.SplashRepositoryImpl
import com.summer.bookshelf.ui.screens.book.BookListViewModel
import com.summer.bookshelf.ui.screens.splash.SplashViewModel
import com.summer.bookshelf.ui.screens.user.frags.LoginViewModel
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

    fun provideSplashRepository(preference: Preference): SplashRepository =
        SplashRepositoryImpl(preference = preference)

    fun provideSignUpRepository(
        countriesService: CountriesService,
        ipService: IPService,
        appDao: AppDao,
        preference: Preference
    ): LoginRepository = LoginRepositoryImpl(
        countriesService = countriesService,
        ipService = ipService,
        appDao = appDao,
        preference = preference
    )

    fun provideBookRepository(
        bookService: BookService,
        appDao: AppDao,
        preference: Preference
    ): BookRepository =
        BookRepositoryImpl(bookService = bookService, appDao = appDao, preference = preference)

    single { provideSplashRepository(preference = get()) }

    single {
        provideSignUpRepository(
            countriesService = get(), ipService = get(), appDao = get(), preference = get()
        )
    }

    single {
        provideBookRepository(bookService = get(), appDao = get(), preference = get())
    }
}

val viewModelModule = module {
    viewModel {
        SplashViewModel(splashRepository = get())
    }
    viewModel {
        LoginViewModel(loginRepository = get())
    }
    viewModel {
        RegisterViewModel(loginRepository = get())
    }
    viewModel {
        BookListViewModel(bookRepository = get())
    }
}