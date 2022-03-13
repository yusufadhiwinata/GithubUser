package com.example.github.ui.detail

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.github.R
import com.example.github.adapter.ViewPagerAdapter
import com.example.github.api.ApiConfig
import com.example.github.api.ApiHelper
import com.example.github.databinding.ActivityDetailBinding
import com.example.github.ui.base.MyViewModelFactory
import com.example.github.utils.Status
import com.google.android.material.tabs.TabLayoutMediator

class DetailActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_USERNAME = "EXTRA_USERNAME"
    }

    private lateinit var binding: ActivityDetailBinding
    private lateinit var viewModel: DetailViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupViewModel()
        setSupportActionBar(binding.detailToolbar)
        supportActionBar?.apply {
            title = resources.getString(R.string.detail_user)
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }
        val username = intent.getStringExtra(EXTRA_USERNAME)
        val bundle = Bundle()
        bundle.putString(EXTRA_USERNAME, username)
        val viewPagerAdapter = ViewPagerAdapter(supportFragmentManager, lifecycle, bundle)
        binding.apply {
            viewPager.adapter = viewPagerAdapter
            TabLayoutMediator(tabsLayout, viewPager) { tab, position ->
                when (position) {
                    0 -> {
                        tab.text = getString(R.string.followers)
                    }
                    1 -> {
                        tab.text = getString(R.string.following)
                    }
                }
            }.attach()
        }

        viewModel.setUserDetail(username.toString())
        observer()
    }

    private fun observer() {
        viewModel.detailResponse.observe(this, {
            binding.apply {
                it.let { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {
                            viewUserBackdrop.tvUsername.text = it.data?.login
                            viewUserBackdrop.tvUserFollowers.text = it.data?.followers.toString()
                            viewUserBackdrop.tvUsersFollowing.text = it.data?.following.toString()
                            viewUserBackdrop.tvUsersRepository.text =
                                it.data?.public_repos.toString()
                            viewUserBackdrop.tvUserBio.text = it.data?.bio
                            Glide.with(this@DetailActivity)
                                .load(it.data?.avatar_url)
                                .into(viewUserBackdrop.detailUserAvatar)
                        }
                        Status.LOADING -> {
                            viewUserBackdrop.tvUsersFollowing.text =
                                resources.getString(R.string.sample)
                            viewUserBackdrop.tvUserFollowers.text =
                                resources.getString(R.string.sample)
                            viewUserBackdrop.tvUsersRepository.text =
                                resources.getString(R.string.sample)
                        }
                        Status.ERROR -> {
                            Toast.makeText(this@DetailActivity, "error", Toast.LENGTH_SHORT).show()
                        }

                    }
                }

            }
        })
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(
            this,
            MyViewModelFactory((ApiHelper(ApiConfig.apiService)))
        ).get(DetailViewModel::class.java)
    }

    override fun onSupportNavigateUp(): Boolean {
        super.onBackPressed()
        return super.onSupportNavigateUp()
    }


}