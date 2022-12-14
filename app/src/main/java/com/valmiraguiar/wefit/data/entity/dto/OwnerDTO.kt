package com.valmiraguiar.wefit.data.entity.dto

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class OwnerDTO(
    @SerializedName("avatar_url") val avatarUrl: String
) : Parcelable