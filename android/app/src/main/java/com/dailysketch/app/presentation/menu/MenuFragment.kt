package com.dailysketch.app.presentation.menu

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
import com.dailysketch.app.core.di.ServiceLocator
import com.dailysketch.app.core.navigation.Navigator
import com.dailysketch.app.core.ui.ViewExtensions
import com.dailysketch.app.databinding.FragmentMenuBinding
import com.dailysketch.app.domain.model.PromptPack
import com.dailysketch.app.presentation.dialog.SettingsDialog
import com.dailysketch.app.presentation.game.GameFragment
import kotlinx.coroutines.launch

class MenuFragment : Fragment() {

    private var _binding: FragmentMenuBinding? = null

    private val viewModel: MenuViewModel by viewModels {
        ServiceLocator.factory(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentMenuBinding.inflate(inflater, container, false)
        _binding = binding
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = _binding ?: return

        viewModel.clearSession()

        binding.menuCta.setOnClickListener {
            val host = activity ?: return@setOnClickListener
            Navigator.push(host.supportFragmentManager, GameFragment(), Navigator.TAG_GAME)
        }

        binding.menuSettings.setOnClickListener {
            SettingsDialog().show(parentFragmentManager, SETTINGS_TAG)
        }

        ViewExtensions.staggerIn(
            listOf(
                binding.menuWordmark,
                binding.menuPromptCard,
                binding.menuSprite,
                binding.menuCalendar,
                binding.menuStatsRow,
                binding.menuCta
            ),
            resources.getDimensionPixelSize(R.dimen.gap_m).toFloat()
        )

        observeState()
    }

    override fun onResume() {
        super.onResume()
        viewModel.refresh()
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

    private fun render(state: MenuUiState) {
        if (!isAdded) return
        val binding = _binding ?: return
        if (state.loading) return

        binding.menuDate.text = state.dateLine
        binding.menuEyebrowMonth.text = state.monthLabel
        binding.menuPrompt.text = state.promptText
        binding.menuCategory.text = getString(
            R.string.menu_category_format,
            getString(packLabel(state.categoryPack))
        )
        binding.menuBadge.visibility =
            if (state.todayDone && state.reminderMark) View.VISIBLE else View.GONE

        binding.menuStatStreak.bindOrHide(state.streak, getString(R.string.stat_streak))
        binding.menuStatEntries.bindOrHide(state.totalEntries, getString(R.string.stat_entries))
        val visible = listOf(binding.menuStatStreak, binding.menuStatEntries)
            .count { it.isCardVisible() }
        binding.menuStatsRow.visibility = if (visible >= MIN_VISIBLE_STATS) {
            View.VISIBLE
        } else {
            View.GONE
        }

        binding.menuCalendar.bind(state.days, state.leadingBlanks)
    }

    private fun packLabel(pack: PromptPack): Int = when (pack) {
        PromptPack.OBSERVATION -> R.string.pack_observation
        PromptPack.IMAGINATION -> R.string.pack_imagination
        PromptPack.MIXED -> R.string.pack_mixed
    }

    override fun onDestroyView() {
        val binding = _binding
        if (binding != null) {
            ViewExtensions.cancelAll(
                listOf(
                    binding.menuWordmark,
                    binding.menuPromptCard,
                    binding.menuSprite,
                    binding.menuCalendar,
                    binding.menuStatsRow,
                    binding.menuCta
                )
            )
            binding.menuStatStreak.cancelAnimations()
            binding.menuStatEntries.cancelAnimations()
        }
        _binding = null
        super.onDestroyView()
    }

    private companion object {
        const val SETTINGS_TAG = "settings"
        const val MIN_VISIBLE_STATS = 2
    }
}
