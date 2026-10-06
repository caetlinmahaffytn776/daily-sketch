package com.dailysketch.app.presentation.gameover

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.dailysketch.app.R
import com.dailysketch.app.core.config.GameConfig
import com.dailysketch.app.core.di.ServiceLocator
import com.dailysketch.app.core.navigation.Navigator
import com.dailysketch.app.core.ui.ViewExtensions
import com.dailysketch.app.databinding.FragmentGameoverBinding
import com.dailysketch.app.presentation.game.GameFragment
import kotlinx.coroutines.launch

class GameOverFragment : Fragment() {

    private var _binding: FragmentGameoverBinding? = null

    private val viewModel: GameOverViewModel by viewModels {
        ServiceLocator.factory(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentGameoverBinding.inflate(inflater, container, false)
        _binding = binding
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = _binding ?: return

        ViewExtensions.popIn(binding.resultHero, GameConfig.HERO_ENTER_MS)
        ViewExtensions.staggerIn(
            listOf(
                binding.resultHeadline,
                binding.resultSub,
                binding.resultStatsRow,
                binding.resultStrip,
                binding.resultCta
            ),
            resources.getDimensionPixelSize(R.dimen.gap_m).toFloat()
        )

        binding.resultCta.setOnClickListener {
            val host = activity ?: return@setOnClickListener
            Navigator.restartGame(host.supportFragmentManager, GameFragment())
        }

        binding.resultMenu.setOnClickListener {
            val host = activity ?: return@setOnClickListener
            Navigator.backToRoot(host.supportFragmentManager)
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

    private fun render(state: GameOverUiState) {
        if (!isAdded) return
        val binding = _binding ?: return
        if (state.loading) return

        binding.resultSub.text = getString(R.string.result_sub_format, state.streak)
        binding.resultNotes.text = when {
            state.hasNotes -> state.notes
            state.hasPrompt -> state.promptText
            else -> getString(R.string.result_no_notes)
        }

        binding.resultStatStreak.countUpOrHide(state.streak, getString(R.string.stat_streak))
        binding.resultStatEntries.bindOrHide(
            state.totalEntries,
            getString(R.string.stat_entries)
        )
        binding.resultStatMinutes.bindOrHide(
            state.lastMinutes,
            getString(R.string.stat_last_minutes)
        )
        val visible = listOf(
            binding.resultStatStreak,
            binding.resultStatEntries,
            binding.resultStatMinutes
        ).count { it.isCardVisible() }
        binding.resultStatsRow.visibility = if (visible >= MIN_VISIBLE_STATS) {
            View.VISIBLE
        } else {
            View.GONE
        }

        binding.resultStrip.bind(state.entries)
    }

    override fun onDestroyView() {
        val binding = _binding
        if (binding != null) {
            ViewExtensions.cancelAll(
                listOf(
                    binding.resultHero,
                    binding.resultHeadline,
                    binding.resultSub,
                    binding.resultStatsRow,
                    binding.resultStrip,
                    binding.resultCta
                )
            )
            binding.resultStatStreak.cancelAnimations()
            binding.resultStatEntries.cancelAnimations()
            binding.resultStatMinutes.cancelAnimations()
        }
        _binding = null
        super.onDestroyView()
    }

    private companion object {
        const val MIN_VISIBLE_STATS = 2
    }
}
