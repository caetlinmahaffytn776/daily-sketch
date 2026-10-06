package com.dailysketch.app.presentation.dialog

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.fragment.app.DialogFragment
import com.dailysketch.app.R
import com.dailysketch.app.core.di.AppGraph
import com.dailysketch.app.core.di.ServiceLocator
import com.dailysketch.app.databinding.DialogSettingsBinding
import com.dailysketch.app.domain.model.PromptPack
import com.google.android.material.button.MaterialButton

class SettingsDialog : DialogFragment() {

    private var _binding: DialogSettingsBinding? = null

    private var graph: AppGraph? = null

    private var resetArmed = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.Dialog_App_Settings)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = DialogSettingsBinding.inflate(inflater, container, false)
        _binding = binding
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = _binding ?: return
        val resolved = ServiceLocator.provide(requireContext())
        graph = resolved

        binding.settingsClose.setOnClickListener { dismissAllowingStateLoss() }
        binding.settingsDone.setOnClickListener { dismissAllowingStateLoss() }

        binding.settingsReminder.isChecked = resolved.settings.reminderMark
        describeSwitch(binding.settingsReminder.isChecked)
        binding.settingsReminder.setOnCheckedChangeListener { _, checked ->
            resolved.settings.reminderMark = checked
            describeSwitch(checked)
        }

        binding.packObservation.setOnClickListener { choosePack(PromptPack.OBSERVATION) }
        binding.packImagination.setOnClickListener { choosePack(PromptPack.IMAGINATION) }
        binding.packMixed.setOnClickListener { choosePack(PromptPack.MIXED) }
        renderPack(resolved.settings.promptPack)

        binding.settingsReset.setOnClickListener { onResetTapped() }
    }

    private fun choosePack(pack: PromptPack) {
        val resolved = graph ?: return
        resolved.settings.promptPack = pack
        renderPack(pack)
    }

    private fun renderPack(pack: PromptPack) {
        val binding = _binding ?: return
        applyChip(binding.packObservation, pack == PromptPack.OBSERVATION)
        applyChip(binding.packImagination, pack == PromptPack.IMAGINATION)
        applyChip(binding.packMixed, pack == PromptPack.MIXED)
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

    private fun describeSwitch(checked: Boolean) {
        val binding = _binding ?: return
        ViewCompat.setStateDescription(
            binding.settingsReminder,
            getString(if (checked) R.string.state_on else R.string.state_off)
        )
    }

    private fun onResetTapped() {
        val binding = _binding ?: return
        if (!resetArmed) {
            resetArmed = true
            binding.settingsReset.setText(R.string.settings_reset_confirm)
            return
        }
        graph?.journal()?.clear()
        graph?.session?.reset()
        resetArmed = false
        binding.settingsReset.setText(R.string.settings_reset)
        dismissAllowingStateLoss()
    }

    override fun onDestroyView() {
        _binding = null
        graph = null
        super.onDestroyView()
    }
}
