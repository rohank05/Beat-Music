package com.therohankumar

import com.therohankumar.modules.AudioPlayerManager
import com.therohankumar.modules.EmbedUtils
import com.therohankumar.modules.filters.reverb.ReverbPcmAudioFilter
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

class AudioFilterSelectListeners: ListenerAdapter() {
    override fun onStringSelectInteraction(event: StringSelectInteractionEvent) {
        when (event.componentId) {
            "filter_select" -> handleFilterSelection(event)
            "reverb_room_select" -> handleReverbRoomSelection(event)
        }
    }
    
    private fun handleFilterSelection(event: StringSelectInteractionEvent) {
        val guildId = event.guild?.idLong ?: return
        if(!AudioPlayerManager.musicManagerExist(guildId)) return
        val musicManager = AudioPlayerManager.getMusicManager(guildId)
        val selectedValues = event.values
        musicManager.audioFilter.updateFilter {
            isNightcore = selectedValues.contains("nightcore")
            isEcho = selectedValues.contains("echo")
            isEightD = selectedValues.contains("eightd")
            isVibrato = selectedValues.contains("vibrato")
            isTremolo = selectedValues.contains("tremolo")
            isBassBoost = selectedValues.contains("bassboost")
            isReverb = selectedValues.contains("reverb")
        }

        val activeFilters = getActiveFiltersText(selectedValues)

        val updatedEmbed = EmbedUtils.createAudioFilterEmbed(activeFilters, true, "These Filters has been enbaled", event.user)
        event.editMessageEmbeds(updatedEmbed).setComponents().queue()
    }
    
    private fun handleReverbRoomSelection(event: StringSelectInteractionEvent) {
        val guildId = event.guild?.idLong ?: return
        if(!AudioPlayerManager.musicManagerExist(guildId)) return
        val musicManager = AudioPlayerManager.getMusicManager(guildId)
        
        if (event.values.isEmpty()) {
            // No room selected, disable reverb
            musicManager.audioFilter.updateFilter {
                isReverb = false
            }
            
            val updatedEmbed = EmbedBuilder()
                .setTitle("🏛️ Reverb Disabled")
                .setDescription("Reverb effect has been disabled")
                .setColor(EmbedUtils.RED_COLOR)
                .build()
            event.editMessageEmbeds(updatedEmbed).setComponents().queue()
        } else {
            // Room selected, enable reverb with specific preset
            val selectedRoom = event.values[0]
            val preset = try {
                ReverbPcmAudioFilter.RoomPreset.valueOf(selectedRoom)
            } catch (e: IllegalArgumentException) {
                event.reply("❌ Invalid room selection!").setEphemeral(true).queue()
                return
            }
            
            musicManager.audioFilter.updateFilter {
                isReverb = true
            }
            musicManager.audioFilter.updateReverbPreset(preset)
            
            val roomName = preset.name.lowercase().replace('_', ' ').replaceFirstChar { it.uppercase() }
            val updatedEmbed = EmbedBuilder()
                .setTitle("🏛️ Reverb Applied")
                .setDescription("Applied **$roomName** reverb effect")
                .addField("Room Size", "${preset.roomSize}ms", true)
                .addField("Decay", "${(preset.decay * 100).toInt()}%", true)
                .addField("Wet Level", "${(preset.wetLevel * 100).toInt()}%", true)
                .setColor(EmbedUtils.GREEN_COLOR)
                .build()
            event.editMessageEmbeds(updatedEmbed).setComponents().queue()
        }
    }

    override fun onButtonInteraction(event: ButtonInteractionEvent) {
        when (event.componentId) {
            "reset_filters" -> handleResetFilters(event)
            "disable_reverb" -> handleDisableReverb(event)
        }
    }
    
    private fun handleResetFilters(event: ButtonInteractionEvent) {
        val guildId = event.guild?.idLong ?: return
        if(!AudioPlayerManager.musicManagerExist(guildId)) return
        val musicManager = AudioPlayerManager.getMusicManager(guildId)
        musicManager.audioFilter.resetFilters()
        val updatedEmbed = EmbedBuilder()
            .setTitle("Audio Filters")
            .setDescription("Audio Filters has been reset")
            .setColor(EmbedUtils.YELLOW_COLOR)
            .build()
        event.editMessageEmbeds(updatedEmbed).setComponents().queue()
    }
    
    private fun handleDisableReverb(event: ButtonInteractionEvent) {
        val guildId = event.guild?.idLong ?: return
        if(!AudioPlayerManager.musicManagerExist(guildId)) return
        val musicManager = AudioPlayerManager.getMusicManager(guildId)
        musicManager.audioFilter.updateFilter {
            isReverb = false
        }
        val updatedEmbed = EmbedBuilder()
            .setTitle("🏛️ Reverb Disabled")
            .setDescription("Reverb effect has been disabled")
            .setColor(EmbedUtils.RED_COLOR)
            .build()
        event.editMessageEmbeds(updatedEmbed).setComponents().queue()
    }

    private fun getActiveFiltersText(selectedValues: List<String>): String {
        val filterNameMap = mapOf(
            "nightcore" to "Nightcore",
            "eightd" to "8D",
            "vibrato" to "Vibrato",
            "tremolo" to "Tremolo",
            "bassboost" to "Bass Boost",
            "echo" to "Echo",
            "reverb" to "Reverb"
        )

        val activeFilters = selectedValues.mapNotNull { filterNameMap[it] }
        return if (activeFilters.isEmpty()) "No filters active"
        else activeFilters.joinToString(", ")
    }
}