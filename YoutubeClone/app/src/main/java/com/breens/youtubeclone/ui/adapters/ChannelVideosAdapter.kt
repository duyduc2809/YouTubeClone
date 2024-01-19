package com.breens.youtubeclone.ui.adapters

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.breens.youtubeclone.data.models.YoutubeVideos
import com.breens.youtubeclone.databinding.ItemVideoHorizontalBinding
import com.breens.youtubeclone.utils.Format

class ChannelVideosAdapter :
    ListAdapter<YoutubeVideos, ChannelVideosAdapter.ChannelVideosViewHolder>(differCallBack) {

    inner class ChannelVideosViewHolder(val binding: ItemVideoHorizontalBinding) :
        RecyclerView.ViewHolder(binding.root)

    companion object {
        private val differCallBack = object : DiffUtil.ItemCallback<YoutubeVideos>() {
            override fun areItemsTheSame(oldItem: YoutubeVideos, newItem: YoutubeVideos) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: YoutubeVideos, newItem: YoutubeVideos) =
                oldItem == newItem
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChannelVideosViewHolder {
        val itemChannelVideosBinding = ItemVideoHorizontalBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ChannelVideosViewHolder(itemChannelVideosBinding)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ChannelVideosViewHolder, position: Int) {
        val video = getItem(position)
        val thumbnailUrl = video.snippet.thumbnails.high.url
        val videoDuration = Format.convertDuration(video.contentDetails.duration)
        val titleVideo = video.snippet.title
        val videoViews = Format.viewsCount(video.statistics.viewCount.toInt())
        val publishAt = Format.convertPublish(video.snippet.publishedAt)

        holder.binding.apply {
            videoThumbnail.load(thumbnailUrl)
            durationVideo.text = videoDuration
            videoTitle.text = titleVideo
            viewCount.text = videoViews
            publishedTime.text = publishAt
        }


    }
}