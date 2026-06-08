package com.app.audiofocus.domain.usecase

import com.app.audiofocus.domain.model.AudioCandidateForScoring
import com.app.audiofocus.domain.model.DiscoveryClassification
import com.app.audiofocus.domain.model.DiscoveryResult
import javax.inject.Inject

class CalculateAudiobookDiscoveryScoreUseCase @Inject constructor() {

    fun execute(candidate: AudioCandidateForScoring): DiscoveryResult {
        val reasons = mutableListOf<String>()
        val decision = candidate.userDecision
        if (decision == "forced_audiobook") {
            return DiscoveryResult(100, DiscoveryClassification.AUDIOBOOK, listOf("forced by user"))
        }
        if (decision == "hidden" || decision == "rejected") {
            return DiscoveryResult(0, DiscoveryClassification.IGNORED, listOf("hidden by user"))
        }
        val minutes = candidate.durationMs / 60_000
        if (minutes >= 10) {
            reasons += "duration >= 10 minutes product rule"
            return DiscoveryResult(100, DiscoveryClassification.AUDIOBOOK, reasons)
        }

        var score = 0
        score += scoreByDuration(candidate.durationMs, reasons)
        score += scoreByName(candidate.displayName, candidate.title, reasons)
        score += scoreByPath(candidate.relativePath, reasons)
        score += scoreByMetadata(candidate.album, candidate.artist, reasons)
        score += scoreByGroup(candidate, reasons)

        val classification = when {
            score >= 80 -> DiscoveryClassification.AUDIOBOOK
            score >= 60 -> DiscoveryClassification.PROBABLE_AUDIOBOOK
            score >= 40 -> DiscoveryClassification.UNKNOWN
            else -> DiscoveryClassification.MUSIC_OR_IGNORED
        }

        return DiscoveryResult(score.coerceIn(0, 100), classification, reasons)
    }

    private fun scoreByDuration(durationMs: Long, reasons: MutableList<String>): Int {
        val minutes = durationMs / 60_000
        return when {
            minutes < 3 -> {
                reasons += "very short"
                -20
            }
            minutes < 6 -> 0
            minutes < 10 -> 8
            minutes < 20 -> 20
            minutes < 45 -> 30
            else -> {
                reasons += "long duration"
                38
            }
        }
    }

    private fun scoreByName(displayName: String, title: String?, reasons: MutableList<String>): Int {
        val source = "$displayName ${title.orEmpty()}".lowercase()
        val positives = listOf("audiolibro", "audiobook", "chapter", "capitulo", "capitulo", "parte", "curso", "lesson", "lecture", "modulo")
        val negatives = listOf("remix", "karaoke", "ringtone", "alarm", "whatsapp", "voice note", "dj", "radio edit")
        var score = 0
        if (positives.any(source::contains)) {
            reasons += "positive file naming"
            score += 20
        }
        if (negatives.any(source::contains)) {
            reasons += "music-like naming"
            score -= 12
        }
        return score
    }

    private fun scoreByPath(relativePath: String?, reasons: MutableList<String>): Int {
        val path = relativePath.orEmpty().lowercase()
        var score = 0
        if (listOf("audiobooks", "audiolibros", "books", "lectures", "courses", "clases").any(path::contains)) {
            reasons += "positive folder path"
            score += 18
        }
        if (listOf("music", "musica", "whatsapp", "telegram", "recordings", "notifications").any(path::contains)) {
            reasons += "negative folder path"
            score -= 10
        }
        return score
    }

    private fun scoreByMetadata(album: String?, artist: String?, reasons: MutableList<String>): Int {
        val metadata = "${album.orEmpty()} ${artist.orEmpty()}".lowercase()
        return if (listOf("spoken", "audiobook", "podcast", "lecture").any(metadata::contains)) {
            reasons += "spoken metadata"
            10
        } else {
            0
        }
    }

    private fun scoreByGroup(candidate: AudioCandidateForScoring, reasons: MutableList<String>): Int {
        val group = candidate.group ?: return 0
        var score = 0
        if (group.filesInSameFolder >= 3) score += 10
        if (group.averageDurationMs >= 8 * 60_000) score += 8
        if (group.hasSequentialNames) score += 5
        if (group.sharedAlbum) score += 4
        if (group.hasChapterPattern) {
            reasons += "chapter grouping"
            score += 8
        }
        return score
    }

}
