package com.example.github.ui.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.github.adapter.UserAdapter
import com.example.github.api.ApiConfig
import com.example.github.api.ApiHelper
import com.example.github.data.model.searchUser.Item
import com.example.github.databinding.ActivityMainBinding
import com.example.github.ui.base.MyViewModelFactory
import com.example.github.ui.detail.DetailActivity
import com.example.github.utils.Status
import com.facebook.shimmer.ShimmerFrameLayout

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var adapter: UserAdapter
    private lateinit var shimmerLayout: ShimmerFrameLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupViewModel()
        shimmerLayout = ShimmerFrameLayout(this)
        adapter = UserAdapter()
        adapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: Item) {
                Intent(this@MainActivity, DetailActivity::class.java).also {
                    it.putExtra(DetailActivity.EXTRA_USERNAME, data.login)
                    startActivity(it)

                }
            }

        })

        binding.apply {
            rvUser.layoutManager = LinearLayoutManager(this@MainActivity)
            rvUser.setHasFixedSize(true)
            rvUser.adapter = adapter
            searchUser.clearFocus()
            searchUser.setIconifiedByDefault(false)
            searchUser.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
                androidx.appcompat.widget.SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    viewModel.setSearch(query)
                    return false
                }

                override fun onQueryTextChange(newText: String): Boolean {
                    return false
                }

            })

        }
        observer()
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(
            this,
            MyViewModelFactory((ApiHelper(ApiConfig.apiService)))
        ).get(MainViewModel::class.java)
    }


    private fun observer() {
        binding.apply {
            viewModel.listResponse.observe(this@MainActivity, {
                it.let { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {
                            resource.data?.let { users ->
                                shimmerLayout.visibility = View.GONE
                                shimmerLayout.stopShimmer()
                                if (users.isNotEmpty()) {
                                    rvUser.visibility = View.VISIBLE
                                    emptyLayout.viewEmpty.visibility = View.GONE
                                    Log.d("TAG", "searchUser: " + resource.message)
                                    Log.d("TAG", "searchUser: " + resource.status)
                                    Log.d("TAG", "searchUser: " + resource.data)
                                    adapter.differ.submitList(it.data)
                                } else {
                                    emptyData.dataNotFound.visibility = View.VISIBLE
                                }

                            }
                        }
                        Status.LOADING -> {
                            shimmerLayout.visibility = View.VISIBLE
                            shimmerLayout.startShimmer()
                            rvUser.visibility = View.GONE
                            emptyLayout.viewEmpty.visibility = View.GONE

                        }
                        Status.ERROR -> {
                            shimmerLayout.visibility = View.VISIBLE
                            rvUser.visibility = View.GONE
                            Toast.makeText(this@MainActivity, it.message, Toast.LENGTH_LONG).show()

                        }
                    }
                }
            })
        }

    }

    override fun onResume() {
        super.onResume()
        shimmerLayout.startShimmer()
    }

    override fun onPause() {
        shimmerLayout.stopShimmer()
        super.onPause()
    }
}