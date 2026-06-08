package com.app.audiofocus

import com.app.audiofocus.domain.model.AudioCandidateForScoring
import com.app.audiofocus.domain.model.CandidateGroup
import com.app.audiofocus.domain.model.DiscoveryClassification
import com.app.audiofocus.domain.usecase.CalculateAudiobookDiscoveryScoreUseCase
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class CalculateAudiobookDiscoveryScoreUseCaseTest {
    private val useCase = CalculateAudiobookDiscoveryScoreUseCase()

    @Test
    fun returnsAudiobookForStrongSignals() {
        val result = useCase.execute(
            AudioCandidateForScoring(
                displayName = "01_capitulo_atomic_habits.mp3",
                title = "Atomic Habits Chapter 1",
                artist = "James Clear",
                album = "Atomic Habits Audiobook",
                durationMs = 4_200_000,
                relativePath = "Audiobooks/Atomic Habits",
                mimeType = "audio/mpeg",
                sizeBytes = 30_000_000,
                group = CandidateGroup(
                    filesInSameFolder = 12,
                    averageDurationMs = 3_900_000,
                    hasSequentialNames = true,
                    sharedAlbum = true,
                    hasChapterPattern = true,
                ),
                userDecision = null,
            ),
        )

        assertEquals(DiscoveryClassification.AUDIOBOOK, result.classification)
        assertTrue(result.score >= 80)
    }

    @Test
    fun respectsHiddenDecision() {
        val result = useCase.execute(
            AudioCandidateForScoring(
                displayName = "voice_note.mp3",
                title = null,
                artist = null,
                album = null,
                durationMs = 60_000,
                relativePath = "WhatsApp Audio",
                mimeType = "audio/mpeg",
                sizeBytes = 500_000,
                group = null,
                userDecision = "hidden",
            ),
        )

        assertEquals(DiscoveryClassification.IGNORED, result.classification)
        assertEquals(0, result.score)
    }
}
