package com.example.github.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.github.ui.followers.FollowersFragment
import com.example.github.ui.following.FollowingFragment

class ViewPagerAdapter(fm: FragmentManager, lifecycle: Lifecycle, bundle: Bundle) :
    FragmentStateAdapter(fm, lifecycle) {

    private var fragmentBundle: Bundle = bundle

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = FollowersFragment()
            1 -> fragment = FollowingFragment()
        }
        fragment?.arguments = this.fragmentBundle
        return fragment as Fragment
    }

}