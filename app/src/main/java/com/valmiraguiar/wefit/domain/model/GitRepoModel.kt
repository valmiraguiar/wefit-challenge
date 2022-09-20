package com.valmiraguiar.wefit.domain.model

data class GitRepoModel(
    val id: Int,
    val fullName: String,
    val description: String,
    val ownerAvatarUrl: String,
    val stargazersCount: Int,
    val language: String,
    val htmlUrl: String
)