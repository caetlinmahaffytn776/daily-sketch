package com.dailysketch.app.data.sample

import com.dailysketch.app.domain.model.Mood
import com.dailysketch.app.domain.model.PromptPack
import com.dailysketch.app.domain.model.SketchEntry
import com.dailysketch.app.domain.model.SketchPrompt
import java.time.LocalDate

object SampleData {

    val PROMPTS: List<SketchPrompt> = listOf(
        SketchPrompt(1, "Draw the view from the nearest window, ten lines and no more.", PromptPack.OBSERVATION, "Let the horizon do the work."),
        SketchPrompt(2, "Sketch the last thing you held in your left hand.", PromptPack.OBSERVATION, "Weight before outline."),
        SketchPrompt(3, "Invent a door that only opens in autumn.", PromptPack.IMAGINATION, "Hinges can be leaves."),
        SketchPrompt(4, "Fill one page with the same leaf, six times.", PromptPack.OBSERVATION, "Each pass gets looser."),
        SketchPrompt(5, "Draw your cup from below, as a beetle would see it.", PromptPack.OBSERVATION, "Low horizon, tall rim."),
        SketchPrompt(6, "Map the route you walk most often, from memory.", PromptPack.MIXED, "Memory bends distance."),
        SketchPrompt(7, "Sketch a chair that has clearly been sat in for years.", PromptPack.OBSERVATION, "Dents carry the story."),
        SketchPrompt(8, "Draw the sound of the room you are in right now.", PromptPack.IMAGINATION, "Marks, not objects."),
        SketchPrompt(9, "Draw a hand doing something ordinary, without lifting the pencil.", PromptPack.OBSERVATION, "One unbroken line."),
        SketchPrompt(10, "Design a lamp for someone who is afraid of the dark.", PromptPack.IMAGINATION, "Light has a shape."),
        SketchPrompt(11, "Copy the shadow of an object, never the object.", PromptPack.OBSERVATION, "Edges blur at the far end."),
        SketchPrompt(12, "Sketch three clouds and name each of them.", PromptPack.MIXED, "Names change the drawing."),
        SketchPrompt(13, "Draw a bird that has never needed to fly.", PromptPack.IMAGINATION, "Heavy feet, soft eye."),
        SketchPrompt(14, "Fill a page with the texture of the nearest fabric.", PromptPack.OBSERVATION, "Pressure makes the weave."),
        SketchPrompt(15, "Draw the contents of your bag without looking inside.", PromptPack.MIXED, "Wrong is interesting."),
        SketchPrompt(16, "Sketch a staircase that goes somewhere unreasonable.", PromptPack.IMAGINATION, "Keep the treads honest."),
        SketchPrompt(17, "Draw the same mug three times, each one faster.", PromptPack.OBSERVATION, "Sixty, thirty, ten seconds."),
        SketchPrompt(18, "Sketch a plant as if it were an architectural plan.", PromptPack.MIXED, "Top view, clean lines."),
        SketchPrompt(19, "Draw a face you remember but cannot name.", PromptPack.MIXED, "Let the gaps stay gaps."),
        SketchPrompt(20, "Invent a tool for carrying quiet from one room to another.", PromptPack.IMAGINATION, "Handles matter."),
        SketchPrompt(21, "Sketch the corner where two walls and a floor meet.", PromptPack.OBSERVATION, "Three planes, one point."),
        SketchPrompt(22, "Draw a key for a lock that no longer exists.", PromptPack.IMAGINATION, "Teeth tell the story."),
        SketchPrompt(23, "Fill the page with circles until none of them are round.", PromptPack.MIXED, "Drift is the point."),
        SketchPrompt(24, "Draw the folds of a coat hanging on a hook.", PromptPack.OBSERVATION, "Gravity pulls the line."),
        SketchPrompt(25, "Sketch the bridge between two things you own.", PromptPack.IMAGINATION, "It can be rope."),
        SketchPrompt(26, "Draw a kitchen utensil as a portrait, with dignity.", PromptPack.MIXED, "Centre it, light it."),
        SketchPrompt(27, "Record the first five shapes you noticed today.", PromptPack.OBSERVATION, "Order them by size."),
        SketchPrompt(28, "Draw a house for a creature the size of a thimble.", PromptPack.IMAGINATION, "Scale the doorway."),
        SketchPrompt(29, "Sketch rain without drawing a single drop.", PromptPack.MIXED, "Wet surfaces only."),
        SketchPrompt(30, "Draw your own shoe from the side it never shows.", PromptPack.OBSERVATION, "Sole, seam, scuff."),
        SketchPrompt(31, "Invent a flag for the street you live on.", PromptPack.IMAGINATION, "Two colours, one symbol."),
        SketchPrompt(32, "Draw the gaps between the objects on a shelf.", PromptPack.OBSERVATION, "Negative space wins."),
        SketchPrompt(33, "Sketch a machine that folds laundry badly.", PromptPack.IMAGINATION, "Show the failure."),
        SketchPrompt(34, "Fill a page with one continuous spiral, slowly.", PromptPack.MIXED, "Breathe at every turn."),
        SketchPrompt(35, "Draw a tree using only straight lines.", PromptPack.OBSERVATION, "Angles find the bend."),
        SketchPrompt(36, "Sketch the last meal you ate, from memory.", PromptPack.MIXED, "Plate first, food second."),
        SketchPrompt(37, "Design a bench that two strangers would share.", PromptPack.IMAGINATION, "Mind the middle."),
        SketchPrompt(38, "Draw a doorway and whatever light comes through it.", PromptPack.OBSERVATION, "Let the floor glow."),
        SketchPrompt(39, "Sketch an animal built from three kitchen objects.", PromptPack.IMAGINATION, "Keep the joints visible."),
        SketchPrompt(40, "Draw the same window at two different hours.", PromptPack.OBSERVATION, "Only the light changes."),
        SketchPrompt(41, "Fill the page with handwriting that is not words.", PromptPack.MIXED, "Rhythm over meaning."),
        SketchPrompt(42, "Draw the underside of a bridge you have crossed.", PromptPack.MIXED, "Ribs and rivets."),
        SketchPrompt(43, "Sketch a hat for someone who thinks too loudly.", PromptPack.IMAGINATION, "Padding, perhaps."),
        SketchPrompt(44, "Draw a pile of books without drawing any titles.", PromptPack.OBSERVATION, "Spines lean."),
        SketchPrompt(45, "Invent a weather that only happens indoors.", PromptPack.IMAGINATION, "Show its effects."),
        SketchPrompt(46, "Draw a crumpled receipt as a mountain range.", PromptPack.MIXED, "Ridges, valleys, light."),
        SketchPrompt(47, "Sketch the handle of something you use daily.", PromptPack.OBSERVATION, "Where the finish wore off."),
        SketchPrompt(48, "Draw a path that forgets where it was going.", PromptPack.IMAGINATION, "Let it double back."),
        SketchPrompt(49, "Fill a page with the same square, shrinking.", PromptPack.MIXED, "Keep the corners sharp."),
        SketchPrompt(50, "Draw a cat that has been asked to wait.", PromptPack.IMAGINATION, "Tail does the talking."),
        SketchPrompt(51, "Sketch a reflection in anything but a mirror.", PromptPack.OBSERVATION, "Kettles work well."),
        SketchPrompt(52, "Draw the heaviest thing in the room, lightly.", PromptPack.OBSERVATION, "Thin line, full weight."),
        SketchPrompt(53, "Invent an instrument heard only at sunrise.", PromptPack.IMAGINATION, "Small, wooden, warm."),
        SketchPrompt(54, "Draw a street corner using only five marks.", PromptPack.MIXED, "Choose the five carefully."),
        SketchPrompt(55, "Sketch a window box in the middle of winter.", PromptPack.OBSERVATION, "Bare is not empty."),
        SketchPrompt(56, "Draw the inside of a pocket you know well.", PromptPack.MIXED, "Lint counts."),
        SketchPrompt(57, "Invent a knot that cannot be undone politely.", PromptPack.IMAGINATION, "Over, under, again."),
        SketchPrompt(58, "Draw a bowl of fruit after someone took the best one.", PromptPack.MIXED, "Leave the gap."),
        SketchPrompt(59, "Sketch the horizon from memory, twice, and compare.", PromptPack.OBSERVATION, "Trust the second one."),
        SketchPrompt(60, "Draw a lantern for a very short journey.", PromptPack.IMAGINATION, "One flame is enough.")
    )

    fun seedEntries(today: LocalDate): List<SketchEntry> {
        val seeds = listOf(
            SeedRow(1, 6, Mood.SMOOTH, "Kept the lines loose, liked the second pass.", 10),
            SeedRow(2, 5, Mood.PROUD, "The shadow finally sat down where it belonged.", 20),
            SeedRow(3, 4, Mood.MESSY, "Too much graphite, not enough patience.", 5),
            SeedRow(4, 3, Mood.SMOOTH, "Six leaves, each one looser than the last.", 20),
            SeedRow(5, 2, Mood.STUCK, "Gave up on the handle and drew the shadow instead.", 10),
            SeedRow(6, 1, Mood.PROUD, "Short session, but the rim is finally round.", 5)
        )
        val result = ArrayList<SketchEntry>(seeds.size)
        for (seed in seeds) {
            val date = today.minusDays(seed.daysBack.toLong())
            val prompt = PROMPTS[(seed.promptId - 1) % PROMPTS.size]
            result.add(
                SketchEntry(
                    dateIso = date.toString(),
                    promptId = prompt.id,
                    promptText = prompt.text,
                    mood = seed.mood,
                    notes = seed.notes,
                    minutes = seed.minutes,
                    savedAtMillis = date.toEpochDay() * MILLIS_PER_DAY
                )
            )
        }
        return result
    }

    private const val MILLIS_PER_DAY = 86400000L

    private data class SeedRow(
        val promptId: Int,
        val daysBack: Int,
        val mood: Mood,
        val notes: String,
        val minutes: Int
    )
}
