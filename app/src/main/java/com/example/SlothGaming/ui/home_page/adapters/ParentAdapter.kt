package com.example.SlothGaming.ui.home_page.adapters

import ChildAdapter
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.SlothGaming.data.models.Section
import com.example.SlothGaming.databinding.ItemSectionRowBinding

class ParentAdapter(private var sections: List<Section>) :
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
            // הגדרת LayoutManager עם Prefetch
            if (layoutManager == null) {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false).apply {
                    initialPrefetchItemCount = 4
                }
            }

            // חיבור ה-ViewPool המשותף
            setRecycledViewPool(viewPool)
            setHasFixedSize(true)
            setItemViewCacheSize(10)

            if (adapter == null) {
                adapter = ChildAdapter(section.items)
            } else {
                // עדכון חכם באמצעות ה-DiffUtil של ה-Child
                (adapter as ChildAdapter).updateData(section.items)
            }
        }
    }

    override fun getItemCount() = sections.size
    fun updateList(newSections: List<Section>) {
        val diffCallback = ParentDiffCallback(sections, newSections)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        sections = newSections
        // זה יעדכן רק את השורות שבהן המשחקים באמת השתנו
        diffResult.dispatchUpdatesTo(this)
    }
}

class ParentDiffCallback(
    private val oldList: List<Section>,
    private val newList: List<Section>
) : DiffUtil.Callback() {
    override fun getOldListSize() = oldList.size
    override fun getNewListSize() = newList.size

    // האם זה אותו Section (לפי הכותרת למשל)
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].title == newList[newItemPosition].title
    }

    // האם תוכן הרשימה בתוך ה-Section השתנה
    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].items == newList[newItemPosition].items
    }
}