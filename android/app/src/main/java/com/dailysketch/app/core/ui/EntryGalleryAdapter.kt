package com.dailysketch.app.core.ui

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.dailysketch.app.R
import com.dailysketch.app.core.util.DateFormatter
import com.dailysketch.app.databinding.ItemEntryCardBinding
import com.dailysketch.app.domain.model.Mood
import com.dailysketch.app.domain.model.SketchEntry

class EntryGalleryAdapter : RecyclerView.Adapter<EntryGalleryAdapter.EntryHolder>() {

    private val items = ArrayList<SketchEntry>()

    fun submit(entries: List<SketchEntry>) {
        items.clear()
        items.addAll(entries)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EntryHolder {
        val binding = ItemEntryCardBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return EntryHolder(binding)
    }

    override fun onBindViewHolder(holder: EntryHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    class EntryHolder(private val binding: ItemEntryCardBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(entry: SketchEntry) {
            val context = binding.root.context
            binding.entryDate.text = DateFormatter.compact(entry.dateIso)
            binding.entryTitle.text = shorten(entry.promptText)
            binding.entryMood.backgroundTintList = ColorStateList.valueOf(
                ContextCompat.getColor(context, moodColor(entry.mood))
            )
            binding.root.contentDescription = context.getString(
                R.string.cd_entry_card,
                DateFormatter.compact(entry.dateIso)
            )
        }

        private fun shorten(text: String): String {
            val words = text.split(" ")
            if (words.size <= WORD_LIMIT) return text
            return words.subList(0, WORD_LIMIT).joinToString(" ")
        }

        private fun moodColor(mood: Mood): Int = when (mood) {
            Mood.SMOOTH -> R.color.plum
            Mood.PROUD -> R.color.terracotta
            Mood.MESSY -> R.color.sand
            Mood.STUCK -> R.color.clay
            Mood.NONE -> R.color.stroke_sand
        }

        private companion object {
            const val WORD_LIMIT = 3
        }
    }
}
