package com.dailysketch.app.core.ui

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dailysketch.app.databinding.ViewStreakStripBinding
import com.dailysketch.app.domain.model.SketchEntry

class StreakStripView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val binding = ViewStreakStripBinding.inflate(LayoutInflater.from(context), this)

    private val adapter = EntryGalleryAdapter()

    init {
        binding.stripList.layoutManager =
            LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        binding.stripList.adapter = adapter
        binding.stripList.isNestedScrollingEnabled = false
    }

    fun bind(entries: List<SketchEntry>) {
        if (entries.isEmpty()) {
            binding.stripList.visibility = View.GONE
            binding.stripEmpty.visibility = View.VISIBLE
            return
        }
        binding.stripList.visibility = View.VISIBLE
        binding.stripEmpty.visibility = View.GONE
        adapter.submit(entries)
    }
}
