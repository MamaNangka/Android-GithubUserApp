package com.k6genap.githubuserapp.data.model.response

import com.google.gson.annotations.SerializedName

data class DetailUserResp (
    @field:SerializedName("login")
    val login: String,
    @field:SerializedName("id")
    val id: Int,
    @field:SerializedName("name")
    val name: String,
    @field:SerializedName("location")
    val location: String,
    @field:SerializedName("public_repos")
    val public_repos: Int,
    @field:SerializedName("company")
    val company: String,
    @field:SerializedName("followers")
    val followers: Int,
    @field:SerializedName("following")
    val following: Int,
    @field:SerializedName("followers_url")
    val followers_url: String,
    @field:SerializedName("following_url")
    val following_url: String,
    @field:SerializedName("avatar_url")
    val avatar_url: String
    )