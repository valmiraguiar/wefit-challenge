package com.valmiraguiar.wefit.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "github_repo_table")
data class GitRepoModel(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "fullname") val fullName: String,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "owner_avatar_url") val ownerAvatarUrl: String,
    @ColumnInfo(name = "stargazers_count") val stargazersCount: Int,
    @ColumnInfo(name = "language") val language: String,
    @ColumnInfo(name = "html_url") val htmlUrl: String
)