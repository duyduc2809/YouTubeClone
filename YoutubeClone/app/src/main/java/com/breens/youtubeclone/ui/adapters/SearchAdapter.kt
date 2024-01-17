package com.breens.youtubeclone.ui.adapters

import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.breens.youtubeclone.data.models.VideosSearch
import com.breens.youtubeclone.data.models.YoutubeVideos
import com.breens.youtubeclone.databinding.ItemVideoBinding
import com.breens.youtubeclone.utils.Format
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

class SearchAdapter(
    private val onItemClickListener: OnItemClickListener
):
    RecyclerView.Adapter<SearchAdapter.PopularVideosViewHolder>() {

    inner class PopularVideosViewHolder(val binding: ItemVideoBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val differCallBack = object : DiffUtil.ItemCallback<VideosSearch>() {
        override fun areItemsTheSame(oldItem: VideosSearch, newItem: VideosSearch) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: VideosSearch, newItem: VideosSearch) =
            oldItem == newItem

    }

    val differ = AsyncListDiffer(this, differCallBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularVideosViewHolder {
        val itemPopularVideosBinding = ItemVideoBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return PopularVideosViewHolder(itemPopularVideosBinding)

    }

    interface OnItemClickListener {
        fun onItemClick(videoId: String)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: PopularVideosViewHolder, position: Int) {
        val video = differ.currentList[position]
        val thumbnailUrl = video.snippet.thumbnails.high.url
        val channelLogo = video.snippet.thumbnails.high.url
        val titleVideo = video.snippet.title
        val channel = video.snippet.channelTitle
        val publishAt = Format.convertPublish(video.snippet.publishedAt)

        holder.binding.apply {
            videoThumbnail.load(thumbnailUrl)
            channelPicture.load(channelLogo)
            videoTitle.text = titleVideo
            channelName.text = channel
            publishedTime.text = publishAt
        }

        holder.itemView.setOnClickListener {
            onItemClickListener.onItemClick(video.id.videoId)
            Log.e("OnItemSearchClick", video.id.videoId)
        }

    }
    override fun getItemCount() = differ.currentList.size
}
