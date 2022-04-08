package com.k6genap.githubuserapp.view.detail.fragments

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.k6genap.githubuserapp.api.RetrofitApiConfig
import com.k6genap.githubuserapp.data.model.User
import com.k6genap.githubuserapp.data.model.response.DetailUserResp
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowersViewModel: ViewModel() {
    val followersLists = MutableLiveData<ArrayList<User>>()


    fun setListFollowers(username: String) {
        val response = RetrofitApiConfig.getApiService().getFollowers(username)
        response.enqueue(object : Callback<ArrayList<User>> {
            override fun onResponse(
                call: Call<ArrayList<User>>,
                response: Response<ArrayList<User>>
            ) {
                if (response.isSuccessful) {
                    followersLists.postValue(response.body())
                }
            }

            override fun onFailure(call: Call<ArrayList<User>>, t: Throwable) {
                t.message?.let { Log.d("Gagal", it) }
            }
        })
    }

    fun getListFollowers(): LiveData<ArrayList<User>>{
        return followersLists
    }

}