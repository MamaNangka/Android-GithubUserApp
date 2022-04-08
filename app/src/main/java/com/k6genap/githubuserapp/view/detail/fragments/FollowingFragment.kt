package com.k6genap.githubuserapp.view.detail.fragments

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.k6genap.githubuserapp.DetailActivity
import com.k6genap.githubuserapp.R
import com.k6genap.githubuserapp.data.model.User
import com.k6genap.githubuserapp.databinding.FragmentFollowingBinding
import com.k6genap.githubuserapp.view.main.UserAdapter

class FollowingFragment : Fragment(R.layout.fragment_following) {

    private var ffgBinding : FragmentFollowingBinding? = null
    private val fgBinding get() = ffgBinding!!
    private lateinit var fgViewModel: FollowingViewModel
    private lateinit var fgAdapter: UserAdapter
    private lateinit var fgUsername: String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args = arguments
        fgUsername = args?.getString(DetailActivity.KEY_USERNAME).toString()

        ffgBinding = FragmentFollowingBinding.bind(view)


        fgAdapter = UserAdapter()
        fgAdapter.notifyDataSetChanged()
        fgAdapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback{
            override fun onItemClicked(data: User) {
                Intent(context, DetailActivity::class.java).also{
                    it.putExtra(DetailActivity.KEY_USERNAME, data.login)
                    it.putExtra(DetailActivity.KEY_ID, data.id)
                    it.putExtra(DetailActivity.KEY_AVATARURL, data.avatar_url)
                    startActivity(it)
                }
            }

        })
        fgBinding.apply {
            rvUser.setHasFixedSize(true)
            rvUser.adapter = fgAdapter
            if (requireActivity().application.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                rvUser.layoutManager = GridLayoutManager(activity, 2)
            } else {
                rvUser.layoutManager = LinearLayoutManager(activity)
            }
        }

        showProgressBar(true)
        fgViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            FollowingViewModel::class.java)
        fgViewModel.setListFollowing(fgUsername)
        fgViewModel.getListFollowing().observe(viewLifecycleOwner, {
            if(it!=null){
                fgAdapter.setList(it)
                showProgressBar(false)
            }
        })
    }

    private  fun showProgressBar(state:Boolean){
        if (state){
            fgBinding.progressBar.visibility = View.VISIBLE
        }else{
            fgBinding.progressBar.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        ffgBinding =null
    }

}