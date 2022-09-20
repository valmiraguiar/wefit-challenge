package com.valmiraguiar.wefit.presentation.githubrepo

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.valmiraguiar.wefit.data.repository.GitRepoRepository
import com.valmiraguiar.wefit.domain.model.GitRepoModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

sealed class GitHubRepoResponse {
    object Loading : GitHubRepoResponse()
    data class Success(val githubRepoList: List<GitRepoModel>) : GitHubRepoResponse()
    data class Error(val e: Throwable) : GitHubRepoResponse()
}

class GitRepoListViewModel(
    private val gitRepository: GitRepoRepository
) : ViewModel() {
    val response: MutableLiveData<GitHubRepoResponse> = MutableLiveData()
    private var currentUser: String = ""

    fun loadRepos(user: String) {
        viewModelScope.launch {
            response.value = GitHubRepoResponse.Loading
            currentUser = user

            withContext(Dispatchers.IO) {
                try {
                    gitRepository.getReposByUser(user).collect() {
                        response.postValue(GitHubRepoResponse.Success(it))
                    }
                } catch (e: Exception) {
                    response.postValue(GitHubRepoResponse.Error(e))
                    Log.e("app ->", "error ${e.message}", e)
                }
            }
        }
    }

    fun saveFavoriteRepo(gitRepoModel: GitRepoModel) {
        viewModelScope.launch {
            try {
                gitRepository.saveFavoriteGitRepo(gitRepoModel)
                loadRepos(currentUser)
            } catch (e: Exception) {
                response.postValue(GitHubRepoResponse.Error(e))
                Log.e("app ->", "error ${e.message}", e)
            }
        }
    }
}