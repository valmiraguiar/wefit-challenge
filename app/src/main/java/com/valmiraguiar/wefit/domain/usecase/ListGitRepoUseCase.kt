package com.valmiraguiar.wefit.domain.usecase

import com.valmiraguiar.wefit.data.repository.GitRepoRepository
import com.valmiraguiar.wefit.domain.model.GitRepoModel
import kotlinx.coroutines.flow.Flow

class ListGitRepoUseCase(
    private val gitRepoRepository: GitRepoRepository
) {
    suspend fun execute(user: String): Flow<List<GitRepoModel>> =
        gitRepoRepository.getReposByUser(user)
}