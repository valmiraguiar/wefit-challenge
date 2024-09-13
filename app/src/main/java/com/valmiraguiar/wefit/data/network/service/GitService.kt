package com.valmiraguiar.wefit.data.network.service

import com.valmiraguiar.wefit.data.network.model.GitRepoDTO
import retrofit2.http.GET
import retrofit2.http.Path

interface GitService {
    @GET("{user}/repos")
    suspend fun getReposByUser(@Path("user") user: String): List<GitRepoDTO>
}