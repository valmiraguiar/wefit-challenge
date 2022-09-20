package com.valmiraguiar.wefit.data.entity.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.valmiraguiar.wefit.domain.model.GitRepoModel

@Dao
interface GitRepoDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveFavoriteGitRepo(gitRepoModel: GitRepoModel)

    @Query("SELECT * FROM github_repo_table WHERE fullname LIKE :user || '%'")
    fun fetchFavoriteRepos(user: String): List<GitRepoModel>

}