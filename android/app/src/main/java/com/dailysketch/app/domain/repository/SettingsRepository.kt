package com.dailysketch.app.domain.repository

import com.dailysketch.app.domain.model.PromptPack

interface SettingsRepository {

    var promptPack: PromptPack

    var reminderMark: Boolean

    var lastOpenedIso: String
}
