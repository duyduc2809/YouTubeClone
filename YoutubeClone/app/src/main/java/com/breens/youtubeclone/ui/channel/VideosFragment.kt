package com.breens.youtubeclone.ui.channel

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.breens.youtubeclone.R
import com.breens.youtubeclone.databinding.FragmentVideosBinding
import com.breens.youtubeclone.ui.adapters.ChannelVideosAdapter
import com.breens.youtubeclone.ui.adapters.PopularVideosAdapter
import com.breens.youtubeclone.ui.viewModels.VideosViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class VideosFragment : Fragment(R.layout.fragment_videos) {
    private var _binding: FragmentVideosBinding? = null
    private val binding get() = _binding!!
    private lateinit var channelVideosAdapter: ChannelVideosAdapter
    private val videosVewModel: VideosViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVideosBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecyclerView()
        observeDataFromYoutubeApi()
    }

    private fun observeDataFromYoutubeApi() {
        videosVewModel.popularVideos.observe(viewLifecycleOwner) { result ->
           channelVideosAdapter.submitList(result.data?.items)
        }
    }

    private fun setUpRecyclerView() {
        channelVideosAdapter = ChannelVideosAdapter()
        binding.videoChannelRecyclerView.apply {
            adapter = channelVideosAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }
    }

}