package com.calamity.rtuitlabclicker.data.remote.dto


import com.google.gson.annotations.SerializedName

data class Plan(
    @SerializedName("collaborators")
    val collaborators: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("private_repos")
    val privateRepos: Int,
    @SerializedName("space")
    val space: Int
)