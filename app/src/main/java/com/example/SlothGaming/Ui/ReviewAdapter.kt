package com.example.SlothGaming.Ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.SlothGaming.data.models.Review
import com.example.SlothGaming.databinding.ReviewLayoutBinding

class ReviewAdapter(private val reviews : List<Review>):
    RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int): ReviewViewHolder =
        ReviewViewHolder(ReviewLayoutBinding
        .inflate(LayoutInflater.from(parent.context),parent,false))


    override fun onBindViewHolder(
        holder: ReviewViewHolder, position: Int)=  holder.bind(reviews[position])


    override fun getItemCount() = reviews.size


    inner class ReviewViewHolder(private val binding : ReviewLayoutBinding):
        RecyclerView.ViewHolder(binding.root){


        fun bind(review : Review){
            binding.gameTitle.text = review.title.toString()
            binding.reviewText.text = review.gameReview.toString()
            binding.givenRating.text = review.rating.toString()
            Glide.with(binding.root).load(review.photo)
                .into(binding.gameImage)
        }

    }
}