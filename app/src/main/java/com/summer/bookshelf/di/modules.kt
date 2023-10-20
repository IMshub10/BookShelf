package com.summer.bookshelf.di

import com.summer.bookshelf.networking.remotes.BookServiceMaker
import com.summer.bookshelf.networking.remotes.CountriesServiceMaker
import com.summer.bookshelf.networking.remotes.IPServiceMaker
import com.summer.bookshelf.screens.main.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val remoteModule = module {
    fun provideBookRemoteModule() = BookServiceMaker()

    fun provideCountriesRemoteModule() = CountriesServiceMaker()

    fun provideIPRemoteModule() = IPServiceMaker()

    single { provideBookRemoteModule() }
    single { provideCountriesRemoteModule() }
    single { provideIPRemoteModule() }
}

val viewModelModule = module {
    viewModel {
        MainViewModel(bookService = get(), countriesService = get(), ipService = get())
    }
}