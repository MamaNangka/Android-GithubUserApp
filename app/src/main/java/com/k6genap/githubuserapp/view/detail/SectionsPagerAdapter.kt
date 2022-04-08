package com.k6genap.githubuserapp.view.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.k6genap.githubuserapp.DetailActivity
import com.k6genap.githubuserapp.view.detail.fragments.FollowersFragment
import com.k6genap.githubuserapp.view.detail.fragments.FollowingFragment


class SectionsPagerAdapter(activity: DetailActivity, data: Bundle) : FragmentStateAdapter(activity) {

    private var fragmentBundle: Bundle

    init{
        fragmentBundle = data
    }

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = FollowersFragment()
            1 -> fragment = FollowingFragment()
        }
        fragment?.arguments = this.fragmentBundle
        return fragment as Fragment
    }

    override fun getItemCount(): Int {
        return 2
    }
}