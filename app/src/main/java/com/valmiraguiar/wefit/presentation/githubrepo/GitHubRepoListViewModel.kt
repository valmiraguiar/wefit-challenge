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
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

sealed class GitHubRepoResponse {
    object Loading : GitHubRepoResponse()
    data class Success(val githubRepoList: List<GitRepoModel>) : GitHubRepoResponse()
    data class Error(val e: Throwable) : GitHubRepoResponse()
}

class GitHubRepoListViewModel : ViewModel(), KoinComponent {
    private val repository by inject<GitRepoRepository>()
    val response: MutableLiveData<GitHubRepoResponse> = MutableLiveData()

    fun loadRepos(user: String) {
        viewModelScope.launch {
            response.value = GitHubRepoResponse.Loading

            withContext(Dispatchers.IO) {
                try {
                    repository.getReposByUser(user).collect() {
                        response.postValue(GitHubRepoResponse.Success(it))
                    }
                } catch (e: Exception) {
                    response.postValue(GitHubRepoResponse.Error(e))
                    Log.e("app ->", "error ${e.message}", e)
                }
            }
        }
    }
}