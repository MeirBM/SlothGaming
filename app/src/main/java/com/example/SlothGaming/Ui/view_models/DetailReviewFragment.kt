package com.example.SlothGaming.Ui.view_models

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import com.bumptech.glide.Glide
import com.example.SlothGaming.databinding.DetailReviewBinding
import kotlin.getValue


class DetailReviewFragment : Fragment() {

    var _binding : DetailReviewBinding?  = null

    private lateinit var player: ExoPlayer

    val viewModel : ReviewViewModel by activityViewModels()

    val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DetailReviewBinding.inflate(inflater,container,false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.chosenReview.observe(viewLifecycleOwner){
            binding.reviewTitle.text=it.title
            binding.reviewDesc.text=it.gameReview
            Glide.with(requireContext()).load(it.photo).circleCrop()
                .into(binding.reviewImage)
        }

        player = ExoPlayer.Builder(requireContext()).build()
        binding.playerView.player = player

        val mediaItem = MediaItem.fromUri(
            Uri.parse("https://www.pornhub.com/view_video.php?viewkey=6504aacd98ccf")
        )

        player.setMediaItem(mediaItem)
        player.prepare()
        player.play()


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }
}