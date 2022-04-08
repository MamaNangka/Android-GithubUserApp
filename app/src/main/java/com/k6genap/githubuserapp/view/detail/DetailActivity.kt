package com.k6genap.githubuserapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.k6genap.githubuserapp.databinding.ActivityDetailBinding
import com.k6genap.githubuserapp.view.detail.DetailViewModel
import com.k6genap.githubuserapp.view.detail.SectionsPagerAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailActivity : AppCompatActivity() {

    companion object{
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tvfollowers,
            R.string.tvfollowing
        )
        const val KEY_USERNAME = "key_username"
        const val KEY_ID = "key_id"
        const val KEY_AVATARURL = "key_avatarurl"
    }
    private lateinit var DetBinding: ActivityDetailBinding
    private lateinit var DetViewModel: DetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DetBinding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(DetBinding.root)

        val username = intent.getStringExtra(KEY_USERNAME)
        val id = intent.getIntExtra(KEY_ID, 0)
        val avatar_url = intent.getStringExtra(KEY_AVATARURL)
        val bundle = Bundle()
        bundle.putString(KEY_USERNAME, username)

        DetViewModel = ViewModelProvider(this).get(DetailViewModel::class.java)

        if (username != null) {
            DetViewModel.setUserDetail(username)
        }
        showProgressBar(true)
        DetViewModel.getUserDetail().observe(this, {
            if (it!= null){

                DetBinding.apply{
                    dtUsername.text = it.login
                    dtName.text = it.name
                    dtCompany.text = it.company
                    dtFollowers.text = "${it.followers}"
                    dtFollowing.text = "${it.following}"
                    dtRepository.text = "${it.public_repos}"
                    dtLocation.text = it.location
                    tvFollowers.text = "Followers"
                    tvFollowing.text = "Following"
                    tvRepository.text = "Repositories"
                    Glide.with(this@DetailActivity)
                        .load(it.avatar_url)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .centerCrop()
                        .apply(RequestOptions.circleCropTransform())
                        .into(dtAvatar)

                }
                showProgressBar(false)
            }
        })

        var _isChecked = false
        CoroutineScope(Dispatchers.IO).launch {
            val t = DetViewModel.checkUser(id)
            withContext(Dispatchers.Main) {
                if (t != null) {
                    if (t > 0) {
                        DetBinding.btnFav.isChecked = true
                        _isChecked = true
                    } else {
                        DetBinding.btnFav.isChecked = false
                        _isChecked = false
                    }
                }
            }
        }

        DetBinding.btnFav.setOnClickListener{
            _isChecked= !_isChecked
            if(_isChecked){
                if (username != null) {
                    if (avatar_url != null) {
                        DetViewModel.tambahFavourite(username, id, avatar_url)
                    }
                }
            }else{
                DetViewModel.hapusFavourite(id)
            }
            DetBinding.btnFav.isChecked= _isChecked
        }


        val sectionsPagerAdapter = SectionsPagerAdapter(this, bundle)
        val viewPager: ViewPager2 = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
        supportActionBar?.elevation = 0f
    }

    private  fun showProgressBar(state:Boolean){
        if (state){
            DetBinding.progressBar.visibility = View.VISIBLE
        }else{
            DetBinding.progressBar.visibility = View.GONE
        }
    }

}

