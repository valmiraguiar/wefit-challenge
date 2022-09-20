package com.valmiraguiar.wefit.data.repository

import com.valmiraguiar.wefit.domain.model.GitRepoModel
import kotlinx.coroutines.flow.Flow

interface GitRepoRepository {
    suspend fun getReposByUser(user: String): Flow<List<GitRepoModel>>
}