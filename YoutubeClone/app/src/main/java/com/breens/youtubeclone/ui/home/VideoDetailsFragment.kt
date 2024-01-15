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
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import com.breens.youtubeclone.R
import com.breens.youtubeclone.data.models.YoutubeResponse
import com.breens.youtubeclone.databinding.FragmentVideoDetailsBinding
import com.breens.youtubeclone.utils.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class VideoDetailsFragment : Fragment(R.layout.fragment_video_details) {

    private var _binding: FragmentVideoDetailsBinding? = null
    private val binding get() = _binding!!
    private val videosViewModel: VideosViewModel by viewModels()
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

    private fun displayVideoDetails(videoDetails: YoutubeResponse?) {
        videoDetails?.let {response ->
            val videoItem = response.items.firstOrNull()
            if (videoItem != null) {
                binding.videoTitle.text = videoItem.snippet.title
                binding.viewCount.text = videoItem.statistics.viewCount
                binding.publishedTime.text = videoItem.snippet.publishedAt
                val videoEmbedUrl = "https://www.youtube.com/embed/${videoItem.id}"
                showVideo(videoEmbedUrl)
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