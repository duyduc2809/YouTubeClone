package com.breens.youtubeclone.ui.home

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.breens.youtubeclone.R
import com.breens.youtubeclone.databinding.FragmentSearchBinding
import com.breens.youtubeclone.ui.adapters.SearchAdapter
import com.breens.youtubeclone.ui.viewModels.VideosViewModel
import com.breens.youtubeclone.utils.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : Fragment() {

    private var _binding : FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private lateinit var searchVideosAdapter: SearchAdapter
    private val searchVideosViewModel: VideosViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeDataFromApi()
        setupSearchView()
        setupRecyclerView()
        setupBackButton()
        setupTouchListener()

    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun observeDataFromApi() {
        searchVideosViewModel.searchResult.observe(viewLifecycleOwner) {resource ->
            if (resource is Resource.Success) {
                searchVideosAdapter.differ.submitList(resource.data?.items)
            }
        }
    }
    private fun setupRecyclerView() {
        searchVideosAdapter = SearchAdapter()
        binding.searchRecyclerView.adapter = searchVideosAdapter
        binding.searchRecyclerView.layoutManager = LinearLayoutManager(requireContext())
    }
    private fun setupSearchView() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            @RequiresApi(Build.VERSION_CODES.M)
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrBlank()) {
                    searchVideosViewModel.searchVideosByKeyword(query)
                }

                hideKeyboard()
                return true
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                // Handle text change if needed
                return false
            }
        })
    }
    private fun setupBackButton() {
        binding.backButton.setOnClickListener {
            findNavController().popBackStack(R.id.fragmentHomeScreen, false)
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("ClickableViewAccessibility")
    private fun setupTouchListener() {
        binding.root.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                hideKeyboard()
            }
            false
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun hideKeyboard() {
        val inputMethodManager = requireContext().getSystemService(InputMethodManager::class.java)
        inputMethodManager.hideSoftInputFromWindow(binding.root.windowToken, 0)
    }

}