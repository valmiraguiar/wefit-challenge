package com.valmiraguiar.wefit.presentation.favorite

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.valmiraguiar.wefit.domain.interfaces.repository.GitRepoRepository
import com.valmiraguiar.wefit.domain.model.GitRepoModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

sealed class FavoriteResponse {
    object Loading : FavoriteResponse()
    data class Success(val favoriteList: List<GitRepoModel>) : FavoriteResponse()
    data class Error(val e: Throwable) : FavoriteResponse()
}

class FavoriteViewModel(
    private val gitRepository: GitRepoRepository
) : ViewModel() {
    val response: MutableLiveData<FavoriteResponse> = MutableLiveData()
    private var currentUser: String = ""

    fun loadFavorites(user: String) {
        viewModelScope.launch {
            response.value = FavoriteResponse.Loading
            currentUser = user

            withContext(Dispatchers.IO) {
                try {
                    gitRepository.fetchFavoriteRepos(user).collect() {
                        response.postValue(FavoriteResponse.Success(it))
                    }
                } catch (e: Exception) {
                    response.postValue(FavoriteResponse.Error(e))
                    Log.e("app ->", "error ${e.message}", e)
                }
            }
        }
    }
}