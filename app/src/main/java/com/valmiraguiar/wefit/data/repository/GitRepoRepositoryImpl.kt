package com.valmiraguiar.wefit.data.repository

import com.valmiraguiar.wefit.data.database.dao.GitRepoDAO
import com.valmiraguiar.wefit.data.mapper.toModel
import com.valmiraguiar.wefit.data.network.service.GitService
import com.valmiraguiar.wefit.domain.interfaces.repository.GitRepoRepository
import com.valmiraguiar.wefit.domain.model.GitRepoModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class GitRepoRepositoryImpl(
    private val gitRepoDAO: GitRepoDAO,
    private val api: GitService,
    private val dispatcher: CoroutineContext
) : GitRepoRepository {

    override suspend fun getReposByUser(user: String): Flow<List<GitRepoModel>> = flow {
        val gitRepoListApi = api.getReposByUser(user).toModel()
        val gitRepoListDb = gitRepoDAO.fetchFavoriteRepos(user)

        val resultList: MutableList<GitRepoModel> = mutableListOf()

        gitRepoListApi.map {
            if (!gitRepoListDb.contains(it)) resultList.add(it)
        }
        emit(resultList)
    }

    override suspend fun fetchFavoriteRepos(user: String): Flow<List<GitRepoModel>> = flow {
        emit(gitRepoDAO.fetchFavoriteRepos(user))
    }

    override suspend fun saveFavoriteGitRepo(gitRepoModel: GitRepoModel) {
        withContext(dispatcher) {
            gitRepoDAO.saveFavoriteGitRepo(gitRepoModel)
        }
    }

}