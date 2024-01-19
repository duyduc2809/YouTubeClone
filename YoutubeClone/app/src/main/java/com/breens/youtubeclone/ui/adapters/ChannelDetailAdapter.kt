package com.breens.youtubeclone.ui.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.breens.youtubeclone.ui.channel.PlaylistFragment
import com.breens.youtubeclone.ui.channel.VideosFragment
import java.lang.IllegalArgumentException

class ChannelDetailAdapter(fragmentActivity: FragmentActivity): FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> VideosFragment()
            1 -> PlaylistFragment()
            else -> throw IllegalArgumentException("Invalid tab position")
        }
    }
}