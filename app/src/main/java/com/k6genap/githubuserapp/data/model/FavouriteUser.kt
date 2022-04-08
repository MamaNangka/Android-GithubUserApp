package com.k6genap.githubuserapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable


@Entity(tableName = "favourite_list")
class FavouriteUser (
    @field:SerializedName("login")
    val login: String,
    @PrimaryKey
    @field:SerializedName("id")
    val id: Int,
    @field:SerializedName("avatar_url")
    val avatar_url: String
): Serializable