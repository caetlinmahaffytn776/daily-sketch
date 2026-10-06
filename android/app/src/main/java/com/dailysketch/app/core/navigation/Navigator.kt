package com.dailysketch.app.core.navigation

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.dailysketch.app.R

object Navigator {

    const val TAG_SPLASH = "splash"
    const val TAG_MENU = "menu"
    const val TAG_GAME = "game"
    const val TAG_RESULT = "result"

    fun showRoot(manager: FragmentManager, fragment: Fragment, tag: String): Boolean {
        if (manager.isStateSaved) return false
        manager.beginTransaction()
            .setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
            .replace(R.id.fragment_container, fragment, tag)
            .commit()
        return true
    }

    fun push(manager: FragmentManager, fragment: Fragment, tag: String): Boolean {
        if (manager.isStateSaved) return false
        manager.beginTransaction()
            .setCustomAnimations(
                R.anim.slide_in_right,
                R.anim.slide_out_left,
                R.anim.slide_in_left,
                R.anim.slide_out_right
            )
            .replace(R.id.fragment_container, fragment, tag)
            .addToBackStack(tag)
            .commit()
        return true
    }

    fun pushResult(manager: FragmentManager, fragment: Fragment, tag: String): Boolean {
        if (manager.isStateSaved) return false
        manager.beginTransaction()
            .setCustomAnimations(
                R.anim.fade_in,
                R.anim.fade_out,
                R.anim.fade_in,
                R.anim.fade_out
            )
            .replace(R.id.fragment_container, fragment, tag)
            .addToBackStack(tag)
            .commit()
        return true
    }

    fun backToRoot(manager: FragmentManager): Boolean {
        if (manager.isStateSaved) return false
        manager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        return true
    }

    fun restartGame(manager: FragmentManager, fragment: Fragment): Boolean {
        if (manager.isStateSaved) return false
        manager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        return push(manager, fragment, TAG_GAME)
    }
}
