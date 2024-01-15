package com.breens.youtubeclone.views.ui.home

import com.breens.youtubeclone.ui.home.SharedViewModel
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.breens.youtubeclone.R
import com.breens.youtubeclone.data.models.Categories
import com.breens.youtubeclone.databinding.FragmentHomeScreenBinding
import com.breens.youtubeclone.ui.adapters.PopularVideosAdapter
import com.breens.youtubeclone.ui.home.VideosViewModel
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentHomeScreen: Fragment(R.layout.fragment_home_screen) {
    private var _binding: FragmentHomeScreenBinding? = null
    private val binding get() = _binding!!
    private lateinit var popularVideosAdapter: PopularVideosAdapter
    private val sharedViewModel: SharedViewModel by viewModels()
    private val popularVideosViewModel: VideosViewModel by viewModels()
    private var currentCategoryId: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeScreenBinding.inflate(inflater, container, false)
        hideActionBar()
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecyclerView()
        observeDataFromYoutubeApi()
        transparentStatusBar()
        binding.imageSearchButton.setOnClickListener{
            navigateToSearchFragment()
        }
    }

    private fun observeDataFromYoutubeApi() {
        popularVideosViewModel.popularVideos.observe(viewLifecycleOwner) { result ->
            popularVideosAdapter.submitList(result.data?.items)
        }
        popularVideosViewModel.categoriesList.observe(viewLifecycleOwner) { categoriesList ->
            updateChipGroup(categoriesList)
        }
        popularVideosViewModel.selectedCategoryVideos.observe(viewLifecycleOwner) { result ->
            val categoryId = popularVideosViewModel.getCurrentCategoryId()
            if (categoryId != currentCategoryId.toString()) {
                currentCategoryId = categoryId?.toInt()
                popularVideosAdapter.submitList(result.data?.items)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.R)
    @Suppress("DEPRECATION")
    private fun transparentStatusBar() {
        val window = requireActivity().window
        WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = true
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = ContextCompat.getColor(requireContext(), R.color.white)
    }

    private fun navigateToVideoDetails(videoId: String) {
        val bundle = bundleOf("videoId" to videoId)
        findNavController().navigate(R.id.action_fragmentHomeScreen_to_fragmentVideoDetails, bundle)
    }

    private fun setUpRecyclerView() {
        popularVideosAdapter = PopularVideosAdapter(object : PopularVideosAdapter.OnItemClickListener{
            override fun onItemClick(videoId: String) {
              navigateToVideoDetails(videoId)
            }
        })
        binding.videosRecyclerview.apply {
            adapter = popularVideosAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }
    }

    private fun hideActionBar() {
        (activity as AppCompatActivity).supportActionBar?.hide()
    }

    private fun updateChipGroup(categoriesList: List<Categories>) {

        val chipGroup = binding.chipGroup
        val fixedChip: Chip? = if (chipGroup.childCount > 0) {
            chipGroup.getChildAt(0) as? Chip
        } else {
            null
        }

        chipGroup.removeAllViews()
        fixedChip?.let { chipGroup.addView(it) }
        categoriesList.forEachIndexed { _, category ->
            val chip = createCategoryChip(category)
            chipGroup.addView(chip)
        }

        binding.allChip.setOnClickListener {
            val popularVideos = popularVideosViewModel.getPopularVideosList()
            if (popularVideos != null) {
                popularVideosAdapter.submitList(popularVideos)
            } else {
                popularVideosViewModel.fetchPopularVideos()
            }
        }
    }

    private fun createCategoryChip(category: Categories): Chip {
        val chip = Chip(requireContext())
        chip.text = category.snippet.title
        chip.tag = category.id
        chip.isClickable = true
        chip.setOnClickListener {
            for (i in 0 until binding.chipGroup.childCount) {
                val otherChip = binding.chipGroup.getChildAt(i) as? Chip
                otherChip?.let {
                    it.setChipBackgroundColorResource(R.color.grey)
                    it.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
                }
            }
            val categoryId = it.tag as? String
            categoryId?.let { id ->
                popularVideosViewModel.fetchVideoByCategory(id)
            }
            chip.setChipBackgroundColorResource(R.color.black)
            chip.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
        }
        return chip
    }

    private fun navigateToSearchFragment() {
        val action = FragmentHomeScreenDirections.actionFragmentHomeScreenToFragmentSearch()
        findNavController().navigate(action)
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}