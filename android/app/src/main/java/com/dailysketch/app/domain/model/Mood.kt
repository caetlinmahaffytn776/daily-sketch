package com.dailysketch.app.domain.model

enum class Mood(val id: String) {
    NONE("none"),
    SMOOTH("smooth"),
    MESSY("messy"),
    PROUD("proud"),
    STUCK("stuck");

    companion object {
        fun fromId(value: String): Mood {
            for (mood in values()) {
                if (mood.id == value) return mood
            }
            return NONE
        }
    }
}
