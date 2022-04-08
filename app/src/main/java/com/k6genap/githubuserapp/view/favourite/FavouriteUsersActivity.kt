package com.k6genap.githubuserapp.view.favourite

import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.k6genap.githubuserapp.DetailActivity
import com.k6genap.githubuserapp.data.FavouriteUser
import com.k6genap.githubuserapp.data.model.User
import com.k6genap.githubuserapp.databinding.ActivityFavouriteBinding
import com.k6genap.githubuserapp.view.main.UserAdapter


class FavouriteUsersActivity : AppCompatActivity() {

    private lateinit var FavBinding : ActivityFavouriteBinding
    private lateinit var FavUserAdapter: UserAdapter
    private lateinit var FavViewModel: FavouriteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FavBinding = ActivityFavouriteBinding.inflate(layoutInflater)
        setContentView(FavBinding.root)

        FavUserAdapter = UserAdapter()
        FavUserAdapter.notifyDataSetChanged()

        FavViewModel = ViewModelProvider(this).get(FavouriteViewModel::class.java)

        FavUserAdapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback{
            override fun onItemClicked(data: User) {
                Intent(this@FavouriteUsersActivity, DetailActivity::class.java).also{
                    it.putExtra(DetailActivity.KEY_USERNAME, data.login)
                    it.putExtra(DetailActivity.KEY_ID, data.id)
                    it.putExtra(DetailActivity.KEY_AVATARURL, data.avatar_url)
                    startActivity(it)
                }
            }

        })

        FavBinding.apply{
            rvUserFav.setHasFixedSize(true)
            if (applicationContext.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                rvUserFav.layoutManager = GridLayoutManager(this@FavouriteUsersActivity, 2)
            } else {
                rvUserFav.layoutManager = LinearLayoutManager(this@FavouriteUsersActivity)
            }
            rvUserFav.adapter = FavUserAdapter
            showProgressBar(true)
        }

        FavViewModel.getFavouriteUser()?.observe(this, {
            if(it!=null){
                val favList = mapList(it)
                FavUserAdapter.setList(favList)
                showProgressBar(false)
            }
        })
    }

    private fun mapList(users: List<FavouriteUser>): ArrayList<User> {
        val userLists = ArrayList<User>()
        for (user in users){
            val userMapped = User(user.login, user.id, user.avatar_url)
            userLists.add(userMapped)
        }
        return userLists
    }

    private  fun showProgressBar(state:Boolean){
        if (state){
            FavBinding.progressBar.visibility = View.VISIBLE
        }else{
            FavBinding.progressBar.visibility = View.GONE
        }
    }
}