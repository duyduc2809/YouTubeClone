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
import com.breens.youtubeclone.data.models.YoutubeResponse
import com.breens.youtubeclone.databinding.FragmentVideoDetailsBinding
import com.breens.youtubeclone.ui.viewModels.ChannelsViewModel
import com.breens.youtubeclone.ui.viewModels.VideosViewModel
import com.breens.youtubeclone.utils.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

@AndroidEntryPoint
class VideoDetailsFragment : Fragment(R.layout.fragment_video_details) {

    private var _binding: FragmentVideoDetailsBinding? = null
    private val binding get() = _binding!!
    private val videosViewModel: VideosViewModel by viewModels()
    private val channelsViewModel: ChannelsViewModel by viewModels()
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
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun displayVideoDetails(videoDetails: YoutubeResponse?) {
        videoDetails?.let {response ->
            val videoItem = response.items.firstOrNull()
            if (videoItem != null) {
                val channelId = videoItem.snippet.channelId
                binding.videoTitle.text = videoItem.snippet.title
                binding.viewCount.text = viewsCount(videoItem.statistics.viewCount.toInt())
                binding.publishedTime.text = convertPublish(videoItem.snippet.publishedAt)
                binding.description.text = videoItem.snippet.tags[0]
                val videoEmbedUrl = "https://www.youtube.com/embed/${videoItem.id}"
                showVideo(videoEmbedUrl)
                loadChannelDetails(channelId)
            }
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
                binding.subCount.text = subCount(channelItem.statistics.subscriberCount.toInt())
            }
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun showVideo(videoUrl: String) {
        binding.videoView.settings.javaScriptEnabled = true
        binding.videoView.loadUrl(videoUrl)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun convertPublish(publishedDay: String): String {
        val formatPattern = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
        val publishedAt = LocalDateTime.parse(publishedDay, formatPattern)
        val currentDate = LocalDateTime.now().withNano(0)
        val differenceInSeconds = ChronoUnit.SECONDS.between(publishedAt, currentDate)
        val differenceInDays = ChronoUnit.DAYS.between(publishedAt, currentDate)
        val differenceInMonths = ChronoUnit.MONTHS.between(publishedAt, currentDate)
        return formatTimeDifference(differenceInDays, differenceInSeconds, differenceInMonths)
    }

    private fun formatTimeDifference(differenceInDays: Long, differenceInSeconds: Long, differenceInMonths: Long): String {
        val hours = differenceInSeconds / 3600
        return when {
            differenceInDays >= 2 -> "$differenceInDays days ago"
            differenceInDays == 1L -> "1 day ago"
            hours >= 1 -> "$hours hours ago"
            differenceInMonths == 1L -> "1 month ago"
            differenceInMonths > 1 -> "$differenceInMonths months ago"
            else -> "just now"
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun viewsCount(views: Int): String {
        val billion = 1_000_000_000
        val million = 1_000_000
        val thousand = 1_000
        return when {
            views >= billion -> "${views / billion} B views"
            views >= million -> "${views / million} M views"
            views >= thousand -> "${views / thousand} K views"
            else -> "$views views"
        }
    }

    private fun subCount(views: Int): String {
        val billion = 1_000_000_000
        val million = 1_000_000
        val thousand = 1_000
        return when {
            views >= billion -> "${views / billion} B"
            views >= million -> "${views / million} M"
            views >= thousand -> "${views / thousand} K"
            else -> "$views Subs"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}