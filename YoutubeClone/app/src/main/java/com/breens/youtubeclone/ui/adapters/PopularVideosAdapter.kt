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
import com.breens.youtubeclone.databinding.ItemVideoBinding
import com.breens.youtubeclone.utils.Format
import com.breens.youtubeclone.viewModel.ChannelsViewModel

class PopularVideosAdapter(
    private val onItemClickListener: OnItemClickListener,
    private val onChannelLogoClickListener: OnChannelLogoClickListener,
    private val channelsViewModel: ChannelsViewModel
) :
    ListAdapter<YoutubeVideos, PopularVideosAdapter.PopularVideosViewHolder>(differCallBack) {

    inner class PopularVideosViewHolder(val binding: ItemVideoBinding) :
        RecyclerView.ViewHolder(binding.root)

    interface OnItemClickListener {
        fun onItemClick(videoId: String)
    }

    interface OnChannelLogoClickListener {
        fun onChannelLogoClick(channelId: String)
    }

    companion object {
        private val differCallBack = object : DiffUtil.ItemCallback<YoutubeVideos>() {
            override fun areItemsTheSame(oldItem: YoutubeVideos, newItem: YoutubeVideos) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: YoutubeVideos, newItem: YoutubeVideos) =
                oldItem == newItem
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularVideosViewHolder {
        val itemPopularVideosBinding = ItemVideoBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return PopularVideosViewHolder(itemPopularVideosBinding)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: PopularVideosViewHolder, position: Int) {
        val video = getItem(position)
        channelsViewModel.getChannelThumbnailUrl(video.snippet.channelId) { channelLogo ->
            channelLogo?.let {
                holder.binding.channelPicture.load(it)
            }
        }
        val thumbnailUrl = video.snippet.thumbnails.high.url
        val videoDuration = Format.convertDuration(video.contentDetails.duration)
        val titleVideo = video.snippet.title
        val channel = video.snippet.channelTitle
        val videoViews = Format.viewsCount(video.statistics.viewCount.toInt())
        val publishAt = Format.convertPublish(video.snippet.publishedAt)

        holder.binding.apply {
            videoThumbnail.load(thumbnailUrl)

            durationVideo.text = videoDuration
            videoTitle.text = titleVideo
            channelName.text = channel
            views.text = videoViews
            publishedTime.text = publishAt
        }

        holder.itemView.setOnClickListener {
            onItemClickListener.onItemClick(video.id)
        }

        holder.binding.channelPicture.setOnClickListener {
            onChannelLogoClickListener.onChannelLogoClick(video.snippet.channelId)
        }
    }
}