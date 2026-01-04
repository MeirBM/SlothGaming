package com.example.SlothGaming.Ui.reviews_handling

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.SlothGaming.data.models.Review
import com.example.SlothGaming.databinding.ReviewLayoutBinding
import com.example.SlothGaming.utils.ColorProvider

class ReviewAdapter(
    private val reviews: List<Review>,
    val callBack: ReviewListener):
    RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder>() {


    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): ReviewViewHolder =
        ReviewViewHolder(
            ReviewLayoutBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)
        )

    interface ReviewListener {
        fun onReviewClicked(index: Int)
        fun onReviewLongClicked(index: Int)
    }

    override fun onBindViewHolder(
        holder: ReviewViewHolder, position: Int
    ) = holder.bind(reviews[position])


    override fun getItemCount() = reviews.size

    fun reviewAt(position: Int) = reviews[position]
    inner class ReviewViewHolder(private val binding: ReviewLayoutBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener, View.OnLongClickListener {

        init {
            // Force LTR for this row, even when text is Hebrew
            ViewCompat.setLayoutDirection(binding.root, ViewCompat.LAYOUT_DIRECTION_LTR)

            binding.gameTitle.textDirection = View.TEXT_DIRECTION_LTR
            binding.gameTitle.textAlignment = View.TEXT_ALIGNMENT_VIEW_START

            binding.reviewText.textDirection = View.TEXT_DIRECTION_LTR
            binding.reviewText.textAlignment = View.TEXT_ALIGNMENT_VIEW_START

            binding.consoleText.textDirection = View.TEXT_DIRECTION_LTR
            binding.consoleText.textAlignment = View.TEXT_ALIGNMENT_VIEW_START

            binding.givenRating.textDirection = View.TEXT_DIRECTION_LTR
            binding.givenRating.textAlignment = View.TEXT_ALIGNMENT_VIEW_START

            binding.root.setOnClickListener(this)
            binding.root.setOnLongClickListener(this)
        }

        override fun onClick(p0: View?) {
            callBack.onReviewClicked(absoluteAdapterPosition)
        }

        override fun onLongClick(p0: View?): Boolean {
            callBack.onReviewLongClicked(absoluteAdapterPosition)
            return false
        }


        fun bind(review: Review) {
            //get context for color changing
            val context = binding.root.context

            //populate recycler item with data
            binding.gameTitle.text = review.title
            binding.reviewText.text = review.gameReview
            binding.consoleText.text = review.console
            binding.givenRating.text = review.rating.toString()
            Glide.with(binding.root).load(review.photo)
                .into(binding.gameImage)

            // set color to match rating score
            val color = ColorProvider.pickColor(review.rating, context)

            binding.givenRating.compoundDrawablesRelative[0]?.mutate()?.setTint(color)

        }
    }
}