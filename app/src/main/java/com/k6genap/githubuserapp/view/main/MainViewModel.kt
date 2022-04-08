package com.k6genap.githubuserapp.view.main

import android.util.Log
import androidx.lifecycle.*
import com.k6genap.githubuserapp.api.RetrofitApiConfig
import com.k6genap.githubuserapp.data.SettingPreferences
import com.k6genap.githubuserapp.data.model.User
import com.k6genap.githubuserapp.data.model.response.UserResp
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(private val pref: SettingPreferences) : ViewModel() {

    val listUsers = MutableLiveData<ArrayList<User>>()

    fun getThemeSettings(): LiveData<Boolean> {
        return pref.getThemeSetting().asLiveData()
    }

    fun saveThemeSetting(isDarkModeActive: Boolean) {
        viewModelScope.launch {
            pref.saveThemeSetting(isDarkModeActive)
        }
    }

    fun setSearchUsers(query: String) {
        val response = RetrofitApiConfig.getApiService().getSearchUsers(query)
        response.enqueue(object : Callback<UserResp> {
            override fun onResponse(call: Call<UserResp>, response: Response<UserResp>) {
                if (response.isSuccessful) {
                    listUsers.postValue(response.body()?.items)
                }
            }

            override fun onFailure(call: Call<UserResp>, t: Throwable) {
                t.message?.let { Log.d("Gagal", it) }
            }
        })
    }

    fun getSearchUsers(): LiveData<ArrayList<User>> {
        return listUsers
    }
}