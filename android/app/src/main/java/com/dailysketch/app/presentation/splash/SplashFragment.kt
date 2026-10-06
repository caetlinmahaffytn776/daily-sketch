package com.dailysketch.app.presentation.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.dailysketch.app.R
import com.dailysketch.app.core.di.ServiceLocator
import com.dailysketch.app.core.navigation.Navigator
import com.dailysketch.app.databinding.FragmentSplashBinding
import com.dailysketch.app.presentation.menu.MenuFragment
import kotlinx.coroutines.launch

class SplashFragment : Fragment() {

    private var _binding: FragmentSplashBinding? = null

    private val viewModel: SplashViewModel by viewModels {
        ServiceLocator.factory(requireContext())
    }

    private val animator = SplashAnimator()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentSplashBinding.inflate(inflater, container, false)
        _binding = binding
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = _binding ?: return

        ViewCompat.setStateDescription(
            binding.splashProgress,
            getString(R.string.state_loading)
        )

        val titleOffset = resources.getDimensionPixelSize(R.dimen.gap_m).toFloat()
        val dotTravel = resources.getDimensionPixelSize(R.dimen.gap_s).toFloat()

        animator.enterMark(binding.splashMarkCard)
        animator.breatheMark(binding.splashMarkCard)
        animator.enterTitle(binding.splashTitle, binding.splashSubtitle, titleOffset)
        animator.driftDots(
            listOf(
                binding.splashDotOne,
                binding.splashDotTwo,
                binding.splashDotThree
            ),
            dotTravel
        )
        animator.pulseLabel(binding.splashLoading)
        animator.fadeFooter(binding.splashFooter)

        observeState()
        viewModel.start()
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

    private fun render(state: SplashUiState) {
        if (!isAdded) return
        val binding = _binding ?: return
        if (!state.preparing) {
            binding.splashLoading.alpha = 1f
        }
        if (state.readyToAdvance) {
            handOver()
        }
    }

    private fun handOver() {
        if (!isAdded) return
        if (viewModel.hasHandedOver()) return
        val host = activity ?: return
        val moved = Navigator.showRoot(
            host.supportFragmentManager,
            MenuFragment(),
            Navigator.TAG_MENU
        )
        if (!moved) return
        viewModel.consumeAdvance()
        animator.cancelAll()
    }

    override fun onDestroyView() {
        animator.cancelAll()
        _binding = null
        super.onDestroyView()
    }
}
