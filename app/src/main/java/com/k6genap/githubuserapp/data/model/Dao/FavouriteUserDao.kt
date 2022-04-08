package com.k6genap.githubuserapp.data.model.Dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.k6genap.githubuserapp.data.FavouriteUser

@Dao
interface FavouriteUserDao {
    @Insert
    suspend fun tambahFavourite(favouriteUser: FavouriteUser)

    @Query("SELECT * FROM favourite_list")
    fun getFavourite(): LiveData<List<FavouriteUser>>

    @Query("DELETE FROM favourite_list WHERE favourite_list.id = :id")
    suspend fun hapusFavourite(id: Int): Int

    @Query("SELECT count(*) FROM favourite_list WHERE favourite_list.id = :id")
    suspend fun  checkFavourite(id:Int): Int
}