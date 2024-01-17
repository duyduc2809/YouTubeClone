package com.breens.youtubeclone.ui.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.breens.youtubeclone.data.models.Categories
import com.breens.youtubeclone.data.models.CategoriesResponse
import com.breens.youtubeclone.data.models.SearchResponse
import com.breens.youtubeclone.data.models.YoutubeResponse
import com.breens.youtubeclone.data.models.YoutubeVideos
import com.breens.youtubeclone.repository.VideosRepository
import com.breens.youtubeclone.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class VideosViewModel @Inject constructor(
    private val repository: VideosRepository
): ViewModel() {

    private val _popularVideos: MutableLiveData<Resource<YoutubeResponse>> = MutableLiveData()
    private val _videoDetails: MutableLiveData<Resource<YoutubeResponse>> = MutableLiveData()
    private val _categories: MutableLiveData<Resource<CategoriesResponse>> = MutableLiveData()
    private val _categoriesList: MutableLiveData<List<Categories>> = MutableLiveData()
    private val _selectedCategoryVideos: MutableLiveData<Resource<YoutubeResponse>> = MutableLiveData()
    private val _searchResults: MutableLiveData<Resource<SearchResponse>> = MutableLiveData()
    private var currentCategoryId: String? = null
    private val loadedDataMap: MutableMap<String, YoutubeResponse> = mutableMapOf()
    private var popularVideosList: List<YoutubeVideos>? = null

    var popularVideos: LiveData<Resource<YoutubeResponse>> = _popularVideos
    var videoDetails: LiveData<Resource<YoutubeResponse>> = _videoDetails
    val categoriesList: LiveData<List<Categories>> = _categoriesList
    var selectedCategoryVideos: LiveData<Resource<YoutubeResponse>> = _selectedCategoryVideos
    var searchResult: LiveData<Resource<SearchResponse>> = _searchResults

    init {
        fetchPopularVideos()
        fetchCategories()
    }

    fun fetchPopularVideos() = viewModelScope.launch {
        _popularVideos.postValue(Resource.Loading())
        val response = repository.getPopularVideos()
        val result = handelYoutubeResponse(response)
        if (result is Resource.Success) {
            popularVideosList = result.data?.items
        }
        _popularVideos.postValue(result)
    }


    private fun fetchCategories() = viewModelScope.launch {
        _categories.postValue(Resource.Loading())
        val response = repository.getCategories()
        _categories.postValue(handelCategoriesResponse(response))
    }

    fun fetchVideoByCategory(categoryId: String) = viewModelScope.launch {
        if (categoryId !in loadedDataMap) {
            _selectedCategoryVideos.postValue(Resource.Loading())
            val response = repository.getVideosByCategory(categoryId)
            _selectedCategoryVideos.postValue(handleVideosForCategoryResponse(response))
            response.body()?.let {
                loadedDataMap[categoryId] = it
            }
        } else {
            val videoResponse = loadedDataMap[categoryId]
            if (videoResponse!=null) {
                _selectedCategoryVideos.postValue(Resource.Success(videoResponse))
            } else {
                _selectedCategoryVideos.postValue(Resource.Error("Loaded data for category is null"))
            }

        }

    }

    fun fetchVideoById(id: String) = viewModelScope.launch {
        _videoDetails.postValue(Resource.Loading())
        Log.d("VideosViewModel", "Fetching video by ID: $id")
        val response = repository.getVideoById(id)
        _videoDetails.postValue(handleVideoDetails(response))
    }

    fun searchVideosByKeyword(query: String) = viewModelScope.launch {
        _searchResults.postValue(Resource.Loading())
        val response = repository.getVideosSearchByKeyword(query)
        _searchResults.postValue(handleSearchResponse(response))
    }

    fun getCurrentCategoryId(): String? {
        return currentCategoryId
    }

    fun getPopularVideosList(): List<YoutubeVideos>? {
        return popularVideosList
    }

    private fun handelYoutubeResponse(response: Response<YoutubeResponse>): Resource<YoutubeResponse> {
        if (response.isSuccessful) {
            response.body()?.let { youtubeResponse ->
                return Resource.Success(youtubeResponse)
            }
        }
        return Resource.Error(response.message())
    }

    private fun handelCategoriesResponse(response: Response<CategoriesResponse>): Resource<CategoriesResponse> {
        return if (response.isSuccessful) {
            response.body()?.let { categoriesResponse ->
                _categoriesList.postValue(categoriesResponse.items)
                Resource.Success(categoriesResponse)
            } ?: Resource.Error("Categories response body is null")
        } else {
            Resource.Error(response.message())
        }
    }

    private fun handleVideosForCategoryResponse(response: Response<YoutubeResponse>): Resource<YoutubeResponse> {
        if (response.isSuccessful) {
            response.body()?.let { youtubeResponse ->
                return Resource.Success(youtubeResponse)
            }
        }
        return Resource.Error(response.message())
    }

    private fun handleSearchResponse(response: Response<SearchResponse>): Resource<SearchResponse> {
        if (response.isSuccessful) {
            response.body()?.let { youtubeResponse ->
                return Resource.Success(youtubeResponse)
            }
        }
        return Resource.Error(response.message())
    }

    private fun handleVideoDetails(response: Response<YoutubeResponse>): Resource<YoutubeResponse> {
        if (response.isSuccessful) {
            response.body()?.let { youtubeResponse ->
                return Resource.Success(youtubeResponse)
            }
        }
        return Resource.Error(response.message())
    }

}