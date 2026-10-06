package com.dailysketch.app.data.repository

import com.dailysketch.app.data.local.SettingsStorage
import com.dailysketch.app.domain.model.PromptPack
import com.dailysketch.app.domain.repository.SettingsRepository

class SettingsRepositoryImpl(private val storage: SettingsStorage) : SettingsRepository {

    override var promptPack: PromptPack
        get() = PromptPack.fromId(storage.readPack())
        set(value) {
            storage.writePack(value.id)
        }

    override var reminderMark: Boolean
        get() = storage.readReminder()
        set(value) {
            storage.writeReminder(value)
        }

    override var lastOpenedIso: String
        get() = storage.readLastOpened()
        set(value) {
            storage.writeLastOpened(value)
        }
}
