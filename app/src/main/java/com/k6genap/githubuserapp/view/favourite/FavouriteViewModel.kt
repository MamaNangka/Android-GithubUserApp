package com.k6genap.githubuserapp.view.favourite

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.k6genap.githubuserapp.data.FavouriteUser
import com.k6genap.githubuserapp.data.model.Dao.FavouriteUserDao
import com.k6genap.githubuserapp.data.UserDatabase

class FavouriteViewModel(application: Application): AndroidViewModel(application) {
    private var favouriteDao: FavouriteUserDao?
    private var favouriteDatabase: UserDatabase?

    init{
        favouriteDatabase = UserDatabase.getDatabase(application)
        favouriteDao = favouriteDatabase?.favouriteUserDao()
    }

    fun getFavouriteUser(): LiveData<List<FavouriteUser>>?{
        return favouriteDao?.getFavourite()
    }
}