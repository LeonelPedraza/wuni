package com.app.audiofocus

import com.app.audiofocus.util.formatDuration
import org.junit.Assert.assertEquals
import org.junit.Test

class FormatDurationTest {

    @Test
    fun formatsShortDurationsAsMinutesSeconds() {
        assertEquals("00:09", formatDuration(9_000))
        assertEquals("45:00", formatDuration(2_700_000))
    }

    @Test
    fun formatsLongDurationsAsHoursMinutesSeconds() {
        assertEquals("01:00:00", formatDuration(3_600_000))
        assertEquals("01:30:15", formatDuration(5_415_000))
    }
}
