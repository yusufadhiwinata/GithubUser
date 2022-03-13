package com.example.github.ui.followers

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.github.R
import com.example.github.adapter.UserAdapter
import com.example.github.api.ApiConfig
import com.example.github.api.ApiHelper
import com.example.github.data.model.searchUser.Item
import com.example.github.databinding.FollowersFragmentBinding
import com.example.github.ui.base.MyViewModelFactory
import com.example.github.ui.detail.DetailActivity
import com.example.github.utils.Status
import com.facebook.shimmer.ShimmerFrameLayout

class FollowersFragment : Fragment(R.layout.followers_fragment) {

    private lateinit var binding: FollowersFragmentBinding
    private lateinit var userAdapter: UserAdapter
    private lateinit var username: String
    private lateinit var viewModel: FollowersViewModel
    private lateinit var shimmerFrameLayout: ShimmerFrameLayout


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FollowersFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args = arguments
        shimmerFrameLayout = ShimmerFrameLayout(activity)
        username = args?.getString(DetailActivity.EXTRA_USERNAME).toString()
        binding = FollowersFragmentBinding.bind(view)
        userAdapter = UserAdapter()
        userAdapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: Item) {
                Intent(activity, DetailActivity::class.java).also {
                    it.putExtra(DetailActivity.EXTRA_USERNAME, data.login)
                    startActivity(it)
                }
            }

        })
        binding.apply {
            rvFollowers.setHasFixedSize(true)
            rvFollowers.layoutManager = LinearLayoutManager(activity)
            rvFollowers.adapter = userAdapter
        }
        viewModel = ViewModelProvider(
            this,
            MyViewModelFactory((ApiHelper(ApiConfig.apiService)))
        ).get(FollowersViewModel::class.java)
        viewModel.setFollowersUser(username)
        observer()
    }

    private fun observer() {
        viewModel.listFollowers.observe(viewLifecycleOwner, {
            it.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        resource.data?.let { user ->
                            binding.shimmerLayout.visibility = View.GONE
                            shimmerFrameLayout.stopShimmer()
                            if (user.isNotEmpty()) {
                                binding.rvFollowers.visibility = View.VISIBLE
                                binding.emptyData.dataNotFound.visibility = View.GONE
                                userAdapter.differ.submitList(it.data)

                            } else {
                                binding.emptyData.dataNotFound.visibility = View.VISIBLE
                            }

                        }
                    }
                    Status.LOADING -> {
                        binding.rvFollowers.visibility = View.GONE
                        binding.shimmerLayout.visibility = View.VISIBLE
                        shimmerFrameLayout.startShimmer()
                        binding.emptyData.dataNotFound.visibility = View.GONE
                        binding

                    }
                    Status.ERROR -> {
                        Toast.makeText(activity, it.message, Toast.LENGTH_LONG).show()


                    }
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()
        shimmerFrameLayout.startShimmer()

    }

    override fun onPause() {
        super.onPause()
        shimmerFrameLayout.stopShimmer()
    }


}