package com.breens.youtubeclone.ui.home

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import coil.load
import com.breens.youtubeclone.R
import com.breens.youtubeclone.data.models.ChannelResponse
import com.breens.youtubeclone.data.models.CommentResponse
import com.breens.youtubeclone.data.models.YoutubeResponse
import com.breens.youtubeclone.databinding.FragmentVideoDetailsBinding
import com.breens.youtubeclone.ui.adapters.PopularVideosAdapter
import com.breens.youtubeclone.ui.viewModels.CommentsViewModel
import com.breens.youtubeclone.ui.viewModels.VideosViewModel
import com.breens.youtubeclone.utils.Format
import com.breens.youtubeclone.utils.Resource
import com.breens.youtubeclone.viewModel.ChannelsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class VideoDetailsFragment : Fragment(R.layout.fragment_video_details) {

    private var _binding: FragmentVideoDetailsBinding? = null
    private val binding get() = _binding!!
    private val videosViewModel: VideosViewModel by viewModels()
    private val channelsViewModel: ChannelsViewModel by viewModels()
    private val commentsViewModel: CommentsViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVideoDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }
    @RequiresApi(Build.VERSION_CODES.R)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val videoId = arguments?.getString("videoId")
        if (videoId != null) {
            Log.d("FragmentVideoDetails", "Received Video ID: $videoId")
            videosViewModel.fetchVideoById(videoId)
            commentsViewModel.fetchCommentByVideoId(videoId)
            observeDataFromDataApi()
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun observeDataFromDataApi() {
        videosViewModel.videoDetails.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Success -> {
                    val videoDetails = resource.data
                    displayVideoDetails(videoDetails)
                }
                is Resource.Error -> {
                    val errorMessage = resource.message ?: "Unknown error"
                    Log.e("VideoDetailsFragment", "Error fetching video details: $errorMessage")
                }
                is Resource.Loading -> {
                }
            }
        }
        commentsViewModel.commentInfo.observe(viewLifecycleOwner) { commentResponse ->
            when (commentResponse) {
                is Resource.Success -> {
                    val commentDetails = commentResponse.data
                    displayCommentThread(commentDetails)
                }
                is Resource.Error -> {
                    val errorMessage = commentResponse.message ?: "Unknown error"
                    Log.e("VideoDetailsFragment", "Error fetching comment details: $errorMessage")
                }
                is Resource.Loading -> {
                }
            }
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun displayVideoDetails(videoDetails: YoutubeResponse?) {
        videoDetails?.let {response ->
            val videoItem = response.items.firstOrNull()
            if (videoItem != null) {
                val channelId = videoItem.snippet.channelId
                binding.videoTitle.text = videoItem.snippet.title
                binding.viewCount.text = Format.viewsCount(videoItem.statistics.viewCount.toInt())
                binding.publishedTime.text = Format.convertPublish(videoItem.snippet.publishedAt)

                binding.description.text = videoItem.snippet.tags?.get(0) ?: ""
                val videoEmbedUrl = "https://www.youtube.com/embed/${videoItem.id}"
                binding.commentItem.totalCount.text = videoItem.statistics.commentCount
                showVideo(videoEmbedUrl)
                loadChannelDetails(channelId)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun displayCommentThread(commentResponse: CommentResponse?) {
        commentResponse?.let {
            val commentItem = commentResponse.items[0]
            binding.commentItem.channelAuthPicture.load(commentItem.snippet.topLevelComment.snippet.authorProfileImageUrl)
            binding.commentItem.textDisplay.text = commentItem.snippet.topLevelComment.snippet.textDisplay
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun loadChannelDetails(channelId: String) {

        lifecycleScope.launch {
            channelsViewModel.fetchChannelById(channelId)
        }
        observeChannelData()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun observeChannelData() {
        channelsViewModel.channelInfo.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Success -> {
                    val channelDetails = resource.data
                    displayChannelDetails(channelDetails)
                }
                is Resource.Error -> {
                    val errorMessage = resource.message ?: "Unknown error"
                    Log.e("VideoDetailsFragment", "Error fetching channel details: $errorMessage")
                }
                is Resource.Loading -> {
                    // Handle loading state if needed
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun displayChannelDetails(channelResponse: ChannelResponse?) {
        channelResponse?.let {
            val channelItem = channelResponse.items.firstOrNull()
            if (channelItem != null) {
                binding.channelPicture.load(channelItem.snippet.thumbnails.high.url)
                Log.e("displayChannelLogo", channelItem.snippet.thumbnails.high.url)
                binding.nameChannel.text = channelItem.snippet.title
                binding.subCount.text = Format.subCount(channelItem.statistics.subscriberCount.toInt())
            }
        }
    }


    @SuppressLint("SetJavaScriptEnabled")
    private fun showVideo(videoUrl: String) {
        binding.videoView.settings.javaScriptEnabled = true
        binding.videoView.loadUrl(videoUrl)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}