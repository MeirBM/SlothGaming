package com.example.SlothGaming.ui.home_page.adapters

import ChildAdapter
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.SlothGaming.data.models.GameItem
import com.example.SlothGaming.data.models.Section
import com.example.SlothGaming.databinding.ItemSectionRowBinding

class ParentAdapter(private val gameClick: (GameItem) -> Unit) :
    ListAdapter<Section, ParentAdapter.ParentViewHolder>(SectionDiffCallback()) {

    // Shared ViewPool for all child RecyclerViews to improve performance
    private val viewPool = RecyclerView.RecycledViewPool().apply { setMaxRecycledViews(0, 20) }

    inner class ParentViewHolder(val binding: ItemSectionRowBinding) :
        RecyclerView.ViewHolder(binding.root) {

        // Init the child adapter once per ViewHolder
        private val childAdapter = ChildAdapter(emptyList(), gameClick)

        init {
            binding.childRecyclerView.apply {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false).apply {
                    initialPrefetchItemCount = 4
                }
                setRecycledViewPool(viewPool)
                setHasFixedSize(true)
                adapter = childAdapter
            }
        }

        fun bind(section: Section) {
            binding.sectionTitle.text = section.title
            // Use the ChildAdapter's DiffUtil update method
            childAdapter.updateData(section.items)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ParentViewHolder {
        val binding = ItemSectionRowBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ParentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ParentViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class SectionDiffCallback : DiffUtil.ItemCallback<Section>() {
    override fun areItemsTheSame(oldItem: Section, newItem: Section): Boolean {
        // Sections are the same if they have the same title
        return oldItem.title == newItem.title
    }

    override fun areContentsTheSame(oldSection: Section, newSection: Section): Boolean {
        // Contents are the same if the list of games matches
        return oldSection.items == newSection.items
    }
}