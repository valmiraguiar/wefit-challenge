package com.valmiraguiar.wefit.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.valmiraguiar.wefit.data.database.GitRepoDatabase
import com.valmiraguiar.wefit.domain.interfaces.repository.GitRepoRepository
import com.valmiraguiar.wefit.data.repository.GitRepoRepositoryImpl
import com.valmiraguiar.wefit.data.network.service.GitService
import com.valmiraguiar.wefit.domain.usecase.ListGitRepoUseCase
import com.valmiraguiar.wefit.presentation.favorite.FavoriteViewModel
import com.valmiraguiar.wefit.presentation.githubrepo.GitRepoListViewModel
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.dsl.bind
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object MainModule {

    private const val BASE_URL = "https://api.github.com/users/"

    fun load() {
        loadKoinModules(
            networkModule() +
                    dataModule() +
                    daoModule() +
                    useCaseModule() +
                    viewModelModule()
        )
    }

    private fun networkModule() = module {
        fun gsonProvider() = GsonBuilder().create()

        single {
            createService<GitService>(BASE_URL, gsonProvider())
        }
    }

    private fun dataModule() = module {
        single<GitRepoRepository> {
            GitRepoRepositoryImpl(get(), get(), Dispatchers.IO)
        }.bind()
    }

    private fun useCaseModule() = module {
        factory {
            ListGitRepoUseCase(get())
        }
    }

    private fun daoModule() = module {
        single { GitRepoDatabase.getInstance(androidContext()).gitRepoDAO }
    }

    private fun viewModelModule() = module {
        viewModel {
            GitRepoListViewModel(get())
        }
        viewModel {
            FavoriteViewModel(get())
        }
    }

    private inline fun <reified T> createService(
        baseUrl: String,
        gson: Gson
    ): T {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(T::class.java)
    }
}