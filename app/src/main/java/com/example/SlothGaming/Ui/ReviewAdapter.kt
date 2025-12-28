package com.example.SlothGaming.Ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.SlothGaming.data.models.Review
import com.example.SlothGaming.databinding.ReviewLayoutBinding


class ReviewAdapter(private val reviews: List<Review> , val callBack: ReviewListener):
    RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int): ReviewViewHolder =
        ReviewViewHolder(ReviewLayoutBinding
        .inflate(LayoutInflater.from(parent.context),parent,false))

    interface ReviewListener {
        fun onReviewClicked(index:Int)
        fun onReviewLongClicked(index:Int)
    }


    override fun onBindViewHolder(
        holder: ReviewViewHolder, position: Int)=  holder.bind(reviews[position])


    override fun getItemCount() = reviews.size

    fun reviewAt(position:Int) = reviews[position]
    inner class ReviewViewHolder(private val binding : ReviewLayoutBinding):
        RecyclerView.ViewHolder(binding.root),View.OnClickListener,View.OnLongClickListener {

        init {
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
            binding.gameTitle.text = review.title.toString()
            binding.reviewText.text = review.gameReview.toString()
            binding.consoleText.text = review.console.toString()
            binding.givenRating.text = review.rating.toString()
            Glide.with(binding.root).load(review.photo).into(binding.gameImage)
        }
    }
}