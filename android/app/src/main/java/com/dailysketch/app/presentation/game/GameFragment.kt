package com.dailysketch.app.presentation.game

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.dailysketch.app.R
import com.dailysketch.app.core.di.ServiceLocator
import com.dailysketch.app.core.navigation.Navigator
import com.dailysketch.app.core.util.DateFormatter
import com.dailysketch.app.databinding.FragmentGameBinding
import com.dailysketch.app.domain.model.Mood
import com.dailysketch.app.presentation.gameover.GameOverFragment
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.launch

class GameFragment : Fragment() {

    private var _binding: FragmentGameBinding? = null

    private val viewModel: GameViewModel by viewModels {
        ServiceLocator.factory(requireContext())
    }

    private var notesGuard = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentGameBinding.inflate(inflater, container, false)
        _binding = binding
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = _binding ?: return

        binding.gameBack.setOnClickListener { goBack() }
        binding.gameDiscard.setOnClickListener { goBack() }
        binding.gameCta.setOnClickListener { viewModel.save() }

        binding.moodSmooth.setOnClickListener { viewModel.selectMood(Mood.SMOOTH) }
        binding.moodMessy.setOnClickListener { viewModel.selectMood(Mood.MESSY) }
        binding.moodProud.setOnClickListener { viewModel.selectMood(Mood.PROUD) }
        binding.moodStuck.setOnClickListener { viewModel.selectMood(Mood.STUCK) }

        binding.minutesFive.setOnClickListener { viewModel.selectMinutes(MINUTES_FIVE) }
        binding.minutesTen.setOnClickListener { viewModel.selectMinutes(MINUTES_TEN) }
        binding.minutesTwenty.setOnClickListener { viewModel.selectMinutes(MINUTES_TWENTY) }
        binding.minutesThirty.setOnClickListener { viewModel.selectMinutes(MINUTES_THIRTY) }

        binding.gameNotes.doAfterTextChanged { editable ->
            if (notesGuard) return@doAfterTextChanged
            viewModel.updateNotes(editable?.toString().orEmpty())
        }

        observeState()
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    render(state)
                }
            }
        }
    }

    private fun render(state: GameUiState) {
        if (!isAdded) return
        val binding = _binding ?: return

        binding.gamePrompt.text = state.promptText
        binding.gameElapsed.text = DateFormatter.elapsed(state.elapsedSeconds)

        applyChip(binding.moodSmooth, state.mood == Mood.SMOOTH)
        applyChip(binding.moodMessy, state.mood == Mood.MESSY)
        applyChip(binding.moodProud, state.mood == Mood.PROUD)
        applyChip(binding.moodStuck, state.mood == Mood.STUCK)

        applyChip(binding.minutesFive, state.minutes == MINUTES_FIVE)
        applyChip(binding.minutesTen, state.minutes == MINUTES_TEN)
        applyChip(binding.minutesTwenty, state.minutes == MINUTES_TWENTY)
        applyChip(binding.minutesThirty, state.minutes == MINUTES_THIRTY)

        binding.gameError.visibility = if (state.error) View.VISIBLE else View.GONE
        binding.gameCta.isEnabled = state.canSave
        binding.gameCta.alpha = if (state.canSave) 1f else DISABLED_ALPHA
        ViewCompat.setStateDescription(
            binding.gameCta,
            getString(if (state.saving) R.string.state_saving else R.string.state_ready)
        )

        if (state.finished) {
            openResult()
        }
    }

    private fun applyChip(chip: MaterialButton, selected: Boolean) {
        val context = chip.context
        val fill = if (selected) R.color.sand else R.color.paper
        val stroke = if (selected) R.color.terracotta else R.color.stroke_warm
        val text = if (selected) R.color.ink else R.color.ink_soft
        chip.backgroundTintList =
            ColorStateList.valueOf(ContextCompat.getColor(context, fill))
        chip.setStrokeColorResource(stroke)
        chip.strokeWidth = resources.getDimensionPixelSize(
            if (selected) R.dimen.rule_width else R.dimen.hairline
        )
        chip.setTextColor(ContextCompat.getColor(context, text))
        ViewCompat.setStateDescription(
            chip,
            getString(if (selected) R.string.state_selected else R.string.state_not_selected)
        )
    }

    private fun openResult() {
        if (!isAdded) return
        val host = activity ?: return
        val moved = Navigator.pushResult(
            host.supportFragmentManager,
            GameOverFragment(),
            Navigator.TAG_RESULT
        )
        if (!moved) return
        viewModel.consumeFinish()
    }

    private fun goBack() {
        if (!isAdded) return
        val host = activity ?: return
        if (host.supportFragmentManager.isStateSaved) return
        host.supportFragmentManager.popBackStack()
    }

    override fun onDestroyView() {
        notesGuard = true
        _binding = null
        super.onDestroyView()
    }

    private companion object {
        const val MINUTES_FIVE = 5
        const val MINUTES_TEN = 10
        const val MINUTES_TWENTY = 20
        const val MINUTES_THIRTY = 30
        const val DISABLED_ALPHA = 0.38f
    }
}
