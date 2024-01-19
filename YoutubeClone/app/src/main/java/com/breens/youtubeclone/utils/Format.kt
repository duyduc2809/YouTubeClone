package com.breens.youtubeclone.utils

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

class Format {

    companion object {

        @RequiresApi(Build.VERSION_CODES.O)
        fun convertDuration(duration: String): String {
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
        fun convertPublish(publishedDay: String): String {
            val formatPattern = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
            val publishedAt = LocalDateTime.parse(publishedDay, formatPattern)
            val currentDate = LocalDateTime.now().withNano(0)
            val differenceInSeconds = ChronoUnit.SECONDS.between(publishedAt, currentDate)
            val differenceInDays = ChronoUnit.DAYS.between(publishedAt, currentDate)
            val differenceInMonths = ChronoUnit.MONTHS.between(publishedAt, currentDate)
            return formatTimeDifference(differenceInDays, differenceInSeconds, differenceInMonths)
        }

        fun formatTimeDifference(differenceInDays: Long, differenceInSeconds: Long, differenceInMonths: Long): String {
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
        fun viewsCount(views: Int): String {
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

        fun subCount(views: Int): String {
            val billion = 1_000_000_000
            val million = 1_000_000
            val thousand = 1_000
            return when {
                views >= billion -> "${views / billion} B subs"
                views >= million -> "${views / million} M subs"
                views >= thousand -> "${views / thousand} K subs"
                else -> "$views Subs"
            }
        }
    }
}