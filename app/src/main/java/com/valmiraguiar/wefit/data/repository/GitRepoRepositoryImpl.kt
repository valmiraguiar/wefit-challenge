package com.valmiraguiar.wefit.data.repository

import com.valmiraguiar.wefit.data.mapper.toModel
import com.valmiraguiar.wefit.data.service.GitHubService
import com.valmiraguiar.wefit.domain.model.GitRepoModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GitRepoRepositoryImpl(
    private val api: GitHubService
) : GitRepoRepository {
    override suspend fun getReposByUser(user: String): Flow<List<GitRepoModel>> = flow {
        val gitRepoList = api.getReposByUser(user).toModel()
        emit(gitRepoList)
    }
}