package com.example.SlothGaming.ui.home_page.adapters

import ChildAdapter
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.SlothGaming.data.models.Section
import com.example.SlothGaming.databinding.ItemSectionRowBinding

class ParentAdapter(private val sections: List<Section>) :
    RecyclerView.Adapter<ParentAdapter.ParentViewHolder>() {

    private val viewPool = RecyclerView.RecycledViewPool()

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

        with(holder.binding) {
            sectionTitle.text = section.title

            // Setup Child RecyclerView
            childRecyclerView.apply {
                layoutManager = LinearLayoutManager(
                    context,
                    LinearLayoutManager.HORIZONTAL,
                    false
                ).apply {
                    initialPrefetchItemCount = 4
                }
                adapter = ChildAdapter(section.items)
                setRecycledViewPool(viewPool)
                setHasFixedSize(true)
            }
        }
    }

    override fun getItemCount() = sections.size
}