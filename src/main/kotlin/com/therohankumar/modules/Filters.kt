package com.therohankumar.modules

import com.sedmelluq.discord.lavaplayer.filter.AudioFilter
import com.sedmelluq.discord.lavaplayer.filter.FloatPcmAudioFilter
import com.sedmelluq.discord.lavaplayer.filter.UniversalPcmAudioFilter
import com.sedmelluq.discord.lavaplayer.filter.equalizer.Equalizer
import com.sedmelluq.discord.lavaplayer.format.AudioDataFormat
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer
import com.sedmelluq.discord.lavaplayer.track.AudioTrack
import com.therohankumar.modules.filters.reverb.ReverbPcmAudioFilter
import org.slf4j.LoggerFactory

data class FilterSettings(
    var isNightcore: Boolean = false,
    var isEightD: Boolean = false,
    var isVibrato: Boolean = false,
    var isTremolo: Boolean = false,
    var isBassBoost: Boolean = false,
    var isEcho: Boolean = false,
    var isReverb: Boolean = false,
    var reverbPreset: ReverbPcmAudioFilter.RoomPreset = ReverbPcmAudioFilter.RoomPreset.AUDITORIUM
)

class Filters(private val audioPlayer: AudioPlayer) {
    private val logger = LoggerFactory.getLogger(Filters::class.java)
    private var settings = FilterSettings()

    // Extension property to check if any filter is enabled
    private val isAnyFilterEnabled: Boolean
        get() = with(settings) {
            isNightcore || isEightD || isVibrato || isTremolo || isBassBoost || isEcho || isReverb
        }

    // Function to update individual filter settings
    fun updateFilter(update: FilterSettings.() -> Unit) {
        settings.update()
        updatePlayerFilter()
        logger.debug("Filter settings updated: $settings")
    }
    
    // Function to update reverb preset specifically
    fun updateReverbPreset(preset: ReverbPcmAudioFilter.RoomPreset) {
        settings.reverbPreset = preset
        if (settings.isReverb) {
            updatePlayerFilter()
        }
        logger.debug("Reverb preset updated to: ${preset.name}")
    }

    // Reset all filters to default state
    fun resetFilters() {
        settings = FilterSettings()
        updatePlayerFilter()
        logger.debug("All filters reset to default")
    }

    // Update the audio player's filter chain
    private fun updatePlayerFilter() {
        if (isAnyFilterEnabled) {
            audioPlayer.setFilterFactory(this::buildChain)
            logger.debug("Applied filter chain with enabled filters")
        } else {
            audioPlayer.setFilterFactory(null)
            logger.debug("Disabled all filters")
        }
    }

    private fun buildChain(
        audioTrack: AudioTrack,
        format: AudioDataFormat,
        downstream: UniversalPcmAudioFilter
    ): List<AudioFilter> = buildList {
        var currentFilter: FloatPcmAudioFilter = downstream

        // Apply Bass Boost filter (using standard equalizer)
        if (settings.isBassBoost) {
            val bands = FloatArray(15) { index ->
                when (index) {
                    0 -> 0.25f
                    1 -> 0.15f
                    2 -> 0.10f
                    3 -> 0.05f
                    4 -> 0.02f
                    else -> 0.0f
                }
            }

            Equalizer(format.channelCount, currentFilter, bands).apply {
                currentFilter = this
                add(this)
                logger.debug("Applied bass boost filter")
            }
        }

        // Apply Reverb filter (only this one is implemented)
        if (settings.isReverb) {
            ReverbPcmAudioFilter(currentFilter, format).apply {
                setPreset(settings.reverbPreset)
                currentFilter = this
                add(this)
                logger.debug("Applied reverb filter with preset: ${settings.reverbPreset.name}")
            }
        }

        // Note: Other filters (Nightcore, 8D, Vibrato, Tremolo, Echo) are not implemented
        // due to missing dependencies. They would be added here when dependencies are available.
        if (settings.isNightcore || settings.isEightD || settings.isVibrato || settings.isTremolo || settings.isEcho) {
            logger.warn("Some filters are enabled but not implemented due to missing dependencies")
        }
    }.asReversed()

    // Getter functions for filter states
    fun getFilterStates(): FilterSettings = settings.copy()
}