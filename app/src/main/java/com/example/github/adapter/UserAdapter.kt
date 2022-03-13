package com.example.github.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.github.data.model.searchUser.Item
import com.example.github.databinding.RowItemUserBinding

class UserAdapter : RecyclerView.Adapter<UserAdapter.ViewHolder>() {


    private var onItemClickCallback: OnItemClickCallback? = null


    private val callback = object : DiffUtil.ItemCallback<Item>() {
        override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean {
            return oldItem == newItem
        }

    }

    val differ = AsyncListDiffer(this, callback)

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }


    inner class ViewHolder(private val binding: RowItemUserBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Item) {
            binding.root.setOnClickListener {
                onItemClickCallback?.onItemClicked(item)
            }
            binding.apply {
                Glide.with(itemView)
                    .load(item.avatar_url)
                    .centerCrop()
                    .into(ivAvatar)
                tvName.text = item.login
                tvUserId.text = item.id.toString()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = RowItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = differ.currentList[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = differ.currentList.size

    interface OnItemClickCallback {
        fun onItemClicked(data: Item)
    }
}