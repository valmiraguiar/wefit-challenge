package com.valmiraguiar.wefit.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.valmiraguiar.wefit.data.repository.GitRepoRepository
import com.valmiraguiar.wefit.data.repository.GitRepoRepositoryImpl
import com.valmiraguiar.wefit.data.service.GitHubService
import com.valmiraguiar.wefit.domain.usecase.ListGitRepoUseCase
import org.koin.core.context.loadKoinModules
import org.koin.dsl.bind
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object MainModule {

    private const val BASE_URL = "https://api.github.com/users/"

    fun load() {
        loadKoinModules(networkModule() + dataModule() + useCaseModule())
    }

    private fun networkModule() = module {
        fun gsonProvider() = GsonBuilder().create()

        single {
            createService<GitHubService>(BASE_URL, gsonProvider())
        }
    }

    private fun dataModule() = module {
        single<GitRepoRepository> {
            GitRepoRepositoryImpl(get())
        }.bind()
    }

    private fun useCaseModule() = module {
        factory {
            ListGitRepoUseCase(get())
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