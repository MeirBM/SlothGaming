package com.example.SlothGaming.ui.home_page.adapters

import ChildAdapter
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.SlothGaming.data.models.GameItem
import com.example.SlothGaming.data.models.Section
import com.example.SlothGaming.databinding.ItemSectionRowBinding

class ParentAdapter(private var sections: List<Section>,
                    private var gameClick:(GameItem) -> Unit) :
    RecyclerView.Adapter<ParentAdapter.ParentViewHolder>() {

    private val viewPool = RecyclerView.RecycledViewPool().apply { setMaxRecycledViews(0, 20) }

    inner class ParentViewHolder(val binding: ItemSectionRowBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ParentViewHolder {
        val binding = ItemSectionRowBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ParentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ParentViewHolder, position: Int) {
        val section = sections[position]
        holder.binding.sectionTitle.text = section.title

        holder.binding.childRecyclerView.apply {
            // Config LayoutManager with prefetch
            if (layoutManager == null) {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false).apply {
                    initialPrefetchItemCount = 4
                }
            }

            // Connection of the common ViewPool
            setRecycledViewPool(viewPool)
            setHasFixedSize(true)
            setItemViewCacheSize(10)

            if (adapter == null) {
                Log.d("crash","from parent")
                adapter = ChildAdapter(section.items, gameClick)
            } else {
                // Smart update of the child with DiffUtil
                (adapter as ChildAdapter).updateData(section.items)
            }
        }
    }

    override fun getItemCount() = sections.size
    fun updateList(newSections: List<Section>) {
        val diffCallback = ParentDiffCallback(sections, newSections)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        sections = newSections
        // Update only where there's a change
        diffResult.dispatchUpdatesTo(this)
    }
}

class ParentDiffCallback(
    private val oldList: List<Section>,
    private val newList: List<Section>
) : DiffUtil.Callback() {
    override fun getOldListSize() = oldList.size
    override fun getNewListSize() = newList.size

    // Check if item the same
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].title == newList[newItemPosition].title
    }

    // Check if content the same
    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].items == newList[newItemPosition].items
    }
}