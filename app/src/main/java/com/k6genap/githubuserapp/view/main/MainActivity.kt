package com.k6genap.githubuserapp.view.main

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.isVisible
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.k6genap.githubuserapp.DetailActivity
import com.k6genap.githubuserapp.R
import com.k6genap.githubuserapp.data.SettingPreferences
import com.k6genap.githubuserapp.data.model.User
import com.k6genap.githubuserapp.databinding.ActivityMainBinding
import com.k6genap.githubuserapp.view.favourite.FavouriteUsersActivity

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class MainActivity : AppCompatActivity() {
    private lateinit var MainBinding: ActivityMainBinding
    private lateinit var MainViewModel1: MainViewModel
    private lateinit var MainAdapter: UserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(MainBinding.root)
        showMainDisp(true)

        MainAdapter = UserAdapter()
        MainAdapter.notifyDataSetChanged()

        MainAdapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: User) {
                Intent(this@MainActivity, DetailActivity::class.java).also {
                    it.putExtra(DetailActivity.KEY_USERNAME, data.login)
                    it.putExtra(DetailActivity.KEY_ID, data.id)
                    it.putExtra(DetailActivity.KEY_AVATARURL, data.avatar_url)
                    startActivity(it)
                }
            }

        })
        val pref = SettingPreferences.getInstance(dataStore)
        MainViewModel1 = ViewModelProvider(this, ViewModelFactory(pref)).get(
            MainViewModel::class.java
        )


        MainBinding.apply {
            if (applicationContext.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                rvUser.layoutManager = GridLayoutManager(this@MainActivity, 2)
            } else {
                rvUser.layoutManager = LinearLayoutManager(this@MainActivity)
            }
            rvUser.setHasFixedSize(true)
            rvUser.adapter = MainAdapter

            btnSearch.setOnClickListener {
                searchUser()
            }

            searchIn.setOnKeyListener { _, keyCode, event ->
                if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    searchUser()
                    return@setOnKeyListener true
                }

                return@setOnKeyListener false

            }
        }

        MainViewModel1.getSearchUsers().observe(this, {
            if (it != null) {
                showMainDisp(false)
                MainAdapter.setList(it)
                showProgressBar(false)
            }

        })

        MainViewModel1.getThemeSettings().observe(this,
            { isDarkModeActive: Boolean ->
                if (isDarkModeActive) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }
            })

        MainBinding.favouriteFab.setOnClickListener { view ->
            Intent(this, FavouriteUsersActivity::class.java).also {
                startActivity(it)
            }
        }

    }

    private fun showProgressBar(state: Boolean) {
        if (state) {
            MainBinding.progressBar.visibility = View.VISIBLE
        } else {
            MainBinding.progressBar.visibility = View.GONE
        }
    }

    private fun showMainDisp(state: Boolean){
        if(state){
            MainBinding.mainIv.visibility = View.VISIBLE
            MainBinding.mainTv.visibility = View.VISIBLE
        }else{
            MainBinding.mainIv.visibility = View.GONE
            MainBinding.mainTv.visibility = View.GONE
        }
    }

    private fun searchUser() {
        MainBinding.apply {
            val query = searchIn.text.toString()
            if (query.isEmpty()) return
            showProgressBar(true)
            MainViewModel1.setSearchUsers(query)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {

            R.id.dark_theme_menu ->{
                MainViewModel1.saveThemeSetting(true)
            }
            R.id.light_theme_menu -> {
                MainViewModel1.saveThemeSetting(false)
            }
        }
        return super.onOptionsItemSelected(item)
    }


}