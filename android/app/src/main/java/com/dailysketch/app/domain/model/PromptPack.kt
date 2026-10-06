package com.dailysketch.app.domain.model

enum class PromptPack(val id: String) {
    OBSERVATION("observation"),
    IMAGINATION("imagination"),
    MIXED("mixed");

    companion object {
        fun fromId(value: String): PromptPack {
            for (pack in values()) {
                if (pack.id == value) return pack
            }
            return MIXED
        }
    }
}
