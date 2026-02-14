package com.example.SlothGaming.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.SlothGaming.data.models.GameItem
import com.example.SlothGaming.databinding.ItemSearchCardBinding

class SearchAdapter(
    private val onGameClick: (GameItem) -> Unit
) : ListAdapter<GameItem, SearchAdapter.SearchViewHolder>(SearchDiffCallback()) {

    inner class SearchViewHolder(val binding: ItemSearchCardBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: GameItem) {
            binding.itemTitle.text = item.title
            Glide.with(binding.root)
                .load(item.imageUrl)
                .override(300, 450)
                .into(binding.gamePoster)
            binding.root.setOnClickListener { onGameClick(item) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val binding = ItemSearchCardBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return SearchViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class SearchDiffCallback : DiffUtil.ItemCallback<GameItem>() {
    override fun areItemsTheSame(oldItem: GameItem, newItem: GameItem) = oldItem.id == newItem.id
    override fun areContentsTheSame(oldItem: GameItem, newItem: GameItem) = oldItem == newItem
}
