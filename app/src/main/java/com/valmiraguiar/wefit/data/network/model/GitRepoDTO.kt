package com.valmiraguiar.wefit.data.network.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class GitRepoDTO(
    @SerializedName("id") val id: String,
    @SerializedName("full_name") val fullName: String,
    @SerializedName("description") val description: String?,
    @SerializedName("owner") val owner: OwnerDTO,
    @SerializedName("stargazers_count") val stargazersCount: Int,
    @SerializedName("language") val language: String?,
    @SerializedName("html_url") val htmlUrl: String
) : Parcelable