package com.breens.youtubeclone.ui.adapters

import android.os.Build
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
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

class SearchAdapter:
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
//        val videoViews = viewsCount(video.statistics.viewCount.toInt())
        val publishAt = convert(video.snippet.publishedAt)

        holder.binding.apply {
            videoThumbnail.load(thumbnailUrl)
            channelPicture.load(channelLogo)
            videoTitle.text = titleVideo
            channelName.text = channel
//            views.text = videoViews
            publishedTime.text = publishAt
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun convert(publishedDay: String): String {
        val formatPattern = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
        val publishedAt = LocalDateTime.parse(publishedDay, formatPattern)

        val currentDate = LocalDateTime.now().withNano(0)

        val differenceInSeconds = ChronoUnit.SECONDS.between(publishedAt, currentDate)
        val differenceInDays = ChronoUnit.DAYS.between(publishedAt, currentDate)
        val differenceInMonths = ChronoUnit.MONTHS.between(publishedAt, currentDate)
        return findDifference(differenceInSeconds, differenceInDays, differenceInMonths)
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

    private fun findDifference(differenceInSeconds: Long, differenceInDays: Long, differenceInMonths: Long): String {
        val hours = differenceInSeconds / 3600

        return when {
            differenceInDays in 21..31 -> "3 weeks ago"
            differenceInDays in 14..20 -> "2 weeks ago"
            differenceInDays in 2..13 -> "$differenceInDays days ago"
            differenceInDays in 0..1 -> "$hours hours ago"
            differenceInMonths in 0..1 -> "$differenceInMonths month ago"
            else -> "$differenceInMonths months ago"
        }
    }

    override fun getItemCount() = differ.currentList.size
}
