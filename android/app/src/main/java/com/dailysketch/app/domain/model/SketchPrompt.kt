package com.dailysketch.app.domain.model

data class SketchPrompt(
    val id: Int,
    val text: String,
    val category: PromptPack,
    val hint: String
)
