package com.k6genap.githubuserapp.view.detail.fragments

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.k6genap.githubuserapp.DetailActivity
import com.k6genap.githubuserapp.R
import com.k6genap.githubuserapp.data.model.User
import com.k6genap.githubuserapp.databinding.FragmentFollowersBinding
import com.k6genap.githubuserapp.view.main.UserAdapter

class FollowersFragment : Fragment(R.layout.fragment_followers) {

    private var ffrBinding : FragmentFollowersBinding? = null
    private val frBinding get() = ffrBinding!!
    private lateinit var frViewModel: FollowersViewModel
    private lateinit var frAdapter: UserAdapter
    private lateinit var frUsername: String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args = arguments
        frUsername = args?.getString(DetailActivity.KEY_USERNAME).toString()

        ffrBinding = FragmentFollowersBinding.bind(view)


        frAdapter = UserAdapter()
        frAdapter.notifyDataSetChanged()

        frAdapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback{
            override fun onItemClicked(data: User) {
                Intent(context, DetailActivity::class.java).also{
                    it.putExtra(DetailActivity.KEY_USERNAME, data.login)
                    it.putExtra(DetailActivity.KEY_ID, data.id)
                    it.putExtra(DetailActivity.KEY_AVATARURL, data.avatar_url)

                    startActivity(it)
                }
            }

        })

        frBinding.apply {
            rvUser.setHasFixedSize(true)
            rvUser.adapter = frAdapter
            if (requireActivity().application.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                rvUser.layoutManager = GridLayoutManager(activity, 2)
            } else {
                rvUser.layoutManager = LinearLayoutManager(activity)
            }


        }

        showProgressBar(true)
        frViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            FollowersViewModel::class.java)
        frViewModel.setListFollowers(frUsername)
        frViewModel.getListFollowers().observe(viewLifecycleOwner, {
            if(it!=null){
                frAdapter.setList(it)
                showProgressBar(false)
            }
        })
    }

    private  fun showProgressBar(state:Boolean){
        if (state){
            frBinding.progressBar.visibility = View.VISIBLE
        }else{
            frBinding.progressBar.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        ffrBinding =null
    }

}