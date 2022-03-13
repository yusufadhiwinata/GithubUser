package com.example.github.ui.following

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.github.R
import com.example.github.adapter.UserAdapter
import com.example.github.api.ApiConfig
import com.example.github.api.ApiHelper
import com.example.github.data.model.searchUser.Item
import com.example.github.databinding.FollowingFragmentBinding
import com.example.github.ui.base.MyViewModelFactory
import com.example.github.ui.detail.DetailActivity
import com.example.github.utils.Status
import com.facebook.shimmer.ShimmerFrameLayout

class FollowingFragment : Fragment(R.layout.following_fragment) {

    private lateinit var binding: FollowingFragmentBinding
    private lateinit var userAdapter: UserAdapter
    private lateinit var username: String
    private lateinit var viewModel: FollowingViewModel
    private lateinit var shimmerLayout: ShimmerFrameLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FollowingFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args = arguments
        shimmerLayout = ShimmerFrameLayout(activity)
        username = args?.getString(DetailActivity.EXTRA_USERNAME).toString()
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
            rvFollowing.setHasFixedSize(true)
            rvFollowing.layoutManager = LinearLayoutManager(activity)
            rvFollowing.adapter = userAdapter
        }
        viewModel = ViewModelProvider(
            this,
            MyViewModelFactory((ApiHelper(ApiConfig.apiService)))
        ).get(FollowingViewModel::class.java)
        viewModel.setFollowingUser(username)
        observer()

    }

    private fun observer() {
        viewModel.listFollowing.observe(viewLifecycleOwner, {
            it.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        resource.data?.let { user ->
                            binding.shimmerLayout.visibility = View.GONE
                            shimmerLayout.stopShimmer()
                            if (user.isNotEmpty()) {
                                binding.rvFollowing.visibility = View.VISIBLE
                                userAdapter.differ.submitList(it.data)
                            } else {
                                binding.emptyData.dataNotFound.visibility = View.VISIBLE
                            }


                        }
                    }
                    Status.LOADING -> {
                        shimmerLayout.startShimmer()
                        binding.rvFollowing.visibility = View.GONE
                        binding.shimmerLayout.visibility = View.VISIBLE
                        binding.emptyData.dataNotFound.visibility = View.GONE
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
        shimmerLayout.startShimmer()
    }

    override fun onPause() {
        super.onPause()
        shimmerLayout.stopShimmer()
    }

}