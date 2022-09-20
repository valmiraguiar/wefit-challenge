package com.valmiraguiar.wefit.data.mapper

import com.valmiraguiar.wefit.data.entity.dto.GitRepoDTO
import com.valmiraguiar.wefit.domain.model.GitRepoModel

fun GitRepoDTO.toModel() =
    GitRepoModel(
        id = id.toInt(),
        fullName = fullName,
        description = description ?: "",
        ownerAvatarUrl = owner.avatarUrl,
        stargazersCount = stargazersCount,
        language = language ?: "",
        htmlUrl = htmlUrl
    )

fun List<GitRepoDTO>.toModel(): List<GitRepoModel> =
    this.map {
        it.toModel()
    }