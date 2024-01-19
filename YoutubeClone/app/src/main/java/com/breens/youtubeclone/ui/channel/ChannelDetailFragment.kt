package com.breens.youtubeclone.ui.channel

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import coil.load
import com.breens.youtubeclone.R
import com.breens.youtubeclone.data.models.ChannelResponse
import com.breens.youtubeclone.databinding.FragmentChannelDetailBinding
import com.breens.youtubeclone.ui.adapters.ChannelDetailAdapter
import com.breens.youtubeclone.utils.Format
import com.breens.youtubeclone.utils.Resource
import com.breens.youtubeclone.viewModel.ChannelsViewModel
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChannelDetailFragment : Fragment(R.layout.fragment_channel_detail) {
    private var _binding: FragmentChannelDetailBinding? = null
    private val binding get() = _binding!!
    private val channelsViewModel: ChannelsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChannelDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tabLayout = binding.channelTabLayout
        val viewPager = binding.channelViewPager
        val channelId = arguments?.getString("channelId")

        if (channelId != null) {
            Log.d("ChannelDetails", "Received Channel Id: $channelId")

            channelsViewModel.fetchChannelById(id = channelId)
            observeDataFromDataApi()
        }

        viewPager.adapter = ChannelDetailAdapter(requireActivity())

        TabLayoutMediator(tabLayout, viewPager) { tab, posititon ->
            when (posititon) {
                0 -> tab.text = "Video"
                1 -> tab.text = "Playlist"
            }
        }.attach()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun observeDataFromDataApi() {
        channelsViewModel.channelInfo.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    val channelDetails = response.data
                    displayChannelDetails(channelDetails)
                }
                is Resource.Error -> {
                    val errorMessage = response.message ?: "Unknown error"
                    Log.e("ChannelDetailsFragment", "Error fetching channel details: $errorMessage")
                }
                is Resource.Loading -> {

                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun displayChannelDetails(channelsDetails: ChannelResponse?) {
        channelsDetails?.let {
            val channelItem = channelsDetails.items.firstOrNull()
            if (channelItem != null) {
                binding.channelBanner.load(channelItem.brandingSettings.image.bannerExternalUrl)
                binding.channelLogo.load(channelItem.snippet.thumbnails.high.url)
                binding.channelName.text = channelItem.snippet.title
                binding.subCount.text = Format.subCount(channelItem.statistics.subscriberCount.toInt())
                binding.channelTitle.text = channelItem.snippet.title
                binding.channelDescription.text = channelItem.snippet.description
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}