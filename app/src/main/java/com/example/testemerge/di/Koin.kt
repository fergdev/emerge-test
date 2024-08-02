package com.example.testemerge.di

import com.example.testemerge.api.LoginService
import com.example.testemerge.api.MoviesApiManager
import com.example.testemerge.ui.home.HomeScreen
import com.example.testemerge.ui.home.HomeViewModel
import com.example.testemerge.ui.login.LoginViewModel
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory


private const val BASE_URL = "https://api.themoviedb.org/3/"

val appModule = module {
    single<LoginService> {
        val kotlinJsonAdapterFactory = KotlinJsonAdapterFactory()
        val moshi = Moshi.Builder().addLast(kotlinJsonAdapterFactory).build()
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
        retrofit.create(LoginService::class.java)
    }
    single { MoviesApiManager(get()) }
    viewModel { LoginViewModel(get(), get()) }
    viewModel { HomeViewModel(get(), get()) }
}