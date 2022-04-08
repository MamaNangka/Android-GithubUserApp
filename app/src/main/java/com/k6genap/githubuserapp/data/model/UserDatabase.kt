package com.k6genap.githubuserapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.k6genap.githubuserapp.data.model.Dao.FavouriteUserDao

@Database(
    entities = [FavouriteUser::class],
    version = 1
)
abstract class UserDatabase: RoomDatabase() {
    companion object{
        var INSTANCE : UserDatabase? = null
        fun getDatabase(context: Context): UserDatabase?{
        if (INSTANCE ==null){
            synchronized(UserDatabase::class){
                INSTANCE = Room.databaseBuilder(context.applicationContext, UserDatabase::class.java, "favourite_database").build()
            }
        }
            return INSTANCE
        }
    }
    abstract fun favouriteUserDao(): FavouriteUserDao
}