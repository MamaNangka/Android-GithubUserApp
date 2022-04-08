package com.k6genap.githubuserapp.view.detail

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.k6genap.githubuserapp.api.RetrofitApiConfig
import com.k6genap.githubuserapp.data.FavouriteUser
import com.k6genap.githubuserapp.data.model.Dao.FavouriteUserDao
import com.k6genap.githubuserapp.data.UserDatabase
import com.k6genap.githubuserapp.data.model.response.DetailUserResp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel(application: Application) : AndroidViewModel(application) {

    val listDetailUser = MutableLiveData<DetailUserResp>()
    private var userDao: FavouriteUserDao?
    private var userDatabase: UserDatabase?

    init{
        userDatabase = UserDatabase.getDatabase(application)
        userDao = userDatabase?.favouriteUserDao()
    }


    fun setUserDetail(username: String) {
        val response = RetrofitApiConfig.getApiService().getUserDetail(username)
        response.enqueue(object : Callback<DetailUserResp> {
            override fun onResponse(
                call: Call<DetailUserResp>,
                response: Response<DetailUserResp>
            ) {
                if (response.isSuccessful) {
                    listDetailUser.postValue(response.body())
                }
            }

            override fun onFailure(call: Call<DetailUserResp>, t: Throwable) {
                t.message?.let { Log.d("Gagal", it) }
            }
        })
    }

    fun getUserDetail(): LiveData<DetailUserResp> {
        return listDetailUser
    }
    fun tambahFavourite(username: String, id: Int, avatarUrl: String){
        CoroutineScope(Dispatchers.IO).launch{
            var user = FavouriteUser(username, id, avatarUrl)
            userDao?.tambahFavourite(user)
        }
    }

    suspend fun checkUser(id: Int) = userDao?.checkFavourite(id)

    fun hapusFavourite(id: Int){
        CoroutineScope(Dispatchers.IO).launch{
            userDao?.hapusFavourite(id)
        }
    }
}