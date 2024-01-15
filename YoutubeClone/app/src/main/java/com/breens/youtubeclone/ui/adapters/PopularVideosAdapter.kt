package com.breens.youtubeclone.ui.adapters

import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.breens.youtubeclone.data.models.YoutubeVideos
import com.breens.youtubeclone.databinding.ItemVideoBinding
import com.breens.youtubeclone.views.ui.home.FragmentHomeScreenDirections
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

class PopularVideosAdapter(private val onItemClickListener: OnItemClickListener) :
    ListAdapter<YoutubeVideos, PopularVideosAdapter.PopularVideosViewHolder>(differCallBack) {

    inner class PopularVideosViewHolder(val binding: ItemVideoBinding) :
        RecyclerView.ViewHolder(binding.root)

    interface OnItemClickListener {
        fun onItemClick(videoId: String)
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
        val thumbnailUrl = video.snippet.thumbnails.high.url
        val channelLogo = video.snippet.thumbnails.high.url
        val videoDuration = convertDuration(video.contentDetails.duration)
        val titleVideo = video.snippet.title
        val channel = video.snippet.channelTitle
        val videoViews = viewsCount(video.statistics.viewCount.toInt())
        val publishAt = convertPublish(video.snippet.publishedAt)

        holder.binding.apply {
            videoThumbnail.load(thumbnailUrl)
            channelPicture.load(channelLogo)
            durationVideo.text = videoDuration
            videoTitle.text = titleVideo
            channelName.text = channel
            views.text = videoViews
            publishedTime.text = publishAt
        }

        holder.itemView.setOnClickListener {
            onItemClickListener.onItemClick(video.id)
        }
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
    private fun convertDuration(duration: String): String {
        val regex = Regex("PT(\\d+H)?(\\d+M)?(\\d+S)?")
        val matchResult = regex.find(duration) ?: return "00:00"
        val hours = matchResult.groups[1]?.value?.removeSuffix("H")?.toIntOrNull() ?: 0
        val minutes = matchResult.groups[2]?.value?.removeSuffix("M")?.toIntOrNull() ?: 0
        val seconds = matchResult.groups[3]?.value?.removeSuffix("S")?.toIntOrNull() ?: 0
        val formattedHours = String.format("%02d", hours)
        val formattedMinutes = String.format("%02d", minutes % 60)
        val formattedSeconds = String.format("%02d", seconds)
        return if (hours > 0 || minutes >= 60) {
            "$formattedHours:$formattedMinutes:$formattedSeconds"
        } else {
            "$formattedMinutes:$formattedSeconds"
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
}