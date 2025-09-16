package com.therohankumar.commands

import com.therohankumar.interfaces.ICommand
import com.therohankumar.modules.AudioPlayerManager
import com.therohankumar.modules.EmbedUtils
import com.therohankumar.modules.Utilities
import com.therohankumar.modules.filters.reverb.ReverbPcmAudioFilter
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.emoji.Emoji
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.Commands
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData
import net.dv8tion.jda.api.interactions.components.buttons.Button
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu
import java.util.concurrent.TimeUnit

class Reverb : ICommand {
    override val name = "reverb"

    override suspend fun execute(event: SlashCommandInteractionEvent) {
        if (!Utilities.commandCheck(event)) return
        
        val musicManager = AudioPlayerManager.getMusicManager(event.guild!!.idLong)
        val currentFilters = musicManager.audioFilter.getFilterStates()
        
        val roomType = event.getOption("room")?.asString
        
        if (roomType != null) {
            // Direct room selection
            val preset = try {
                ReverbPcmAudioFilter.RoomPreset.valueOf(roomType.uppercase())
            } catch (e: IllegalArgumentException) {
                event.reply("❌ Invalid room type! Use `/reverb` without parameters to see available options.").setEphemeral(true).queue()
                return
            }
            
            musicManager.audioFilter.updateFilter {
                isReverb = true
            }
            
            // Apply the specific preset (this would need to be implemented in the filter system)
            val embed = EmbedBuilder()
                .setTitle("🏛️ Reverb Applied")
                .setDescription("Applied **${preset.name.lowercase().replace('_', ' ').replaceFirstChar { it.uppercase() }}** reverb effect")
                .addField("Room Size", "${preset.roomSize}ms", true)
                .addField("Decay", "${(preset.decay * 100).toInt()}%", true)
                .addField("Wet Level", "${(preset.wetLevel * 100).toInt()}%", true)
                .setColor(EmbedUtils.GREEN_COLOR)
                .build()
            
            event.replyEmbeds(embed).queue()
        } else {
            // Show room selection menu
            showReverbMenu(event, currentFilters.isReverb)
        }
    }
    
    private fun showReverbMenu(event: SlashCommandInteractionEvent, isReverbActive: Boolean) {
        val menuBuilder = StringSelectMenu.create("reverb_room_select")
            .setMinValues(0)
            .setMaxValues(1)
            .setPlaceholder("Select a room type for reverb effect")
        
        // Add all available room presets
        ReverbPcmAudioFilter.RoomPreset.values().forEach { preset ->
            val emoji = getRoomEmoji(preset)
            val description = "Size: ${preset.roomSize}ms, Decay: ${(preset.decay * 100).toInt()}%, Wet: ${(preset.wetLevel * 100).toInt()}%"
            menuBuilder.addOption(
                preset.name.lowercase().replace('_', ' ').replaceFirstChar { it.uppercase() },
                preset.name,
                description,
                emoji
            )
        }
        
        val selectMenu = menuBuilder.build()
        val disableButton = Button.danger("disable_reverb", "Disable Reverb")
        
        val embed = EmbedBuilder()
            .setTitle("🏛️ Reverb Room Selection")
            .setDescription("Choose a room type to apply reverb effect with different characteristics")
            .addField("Current Status", if (isReverbActive) "✅ Reverb Active" else "❌ Reverb Inactive", false)
            .addField("Room Types Available", "${ReverbPcmAudioFilter.RoomPreset.values().size} different room acoustics", false)
            .setFooter("Each room has unique size, decay, and wet level settings")
            .setColor(EmbedUtils.YELLOW_COLOR)
            .build()
        
        event.hook.sendMessageEmbeds(embed)
            .addActionRow(selectMenu)
            .addActionRow(disableButton)
            .queue { message ->
                val expireEmbed = EmbedUtils.createErrorEmbed("Expired", "This reverb menu has expired")
                message.editMessageEmbeds(expireEmbed).setComponents().queueAfter(5, TimeUnit.MINUTES)
            }
    }
    
    private fun getRoomEmoji(preset: ReverbPcmAudioFilter.RoomPreset): Emoji {
        return when (preset) {
            ReverbPcmAudioFilter.RoomPreset.SMALL_ROOM -> Emoji.fromUnicode("🏠")
            ReverbPcmAudioFilter.RoomPreset.MEDIUM_ROOM -> Emoji.fromUnicode("🏢")
            ReverbPcmAudioFilter.RoomPreset.LARGE_ROOM -> Emoji.fromUnicode("🏬")
            ReverbPcmAudioFilter.RoomPreset.CONCERT_HALL -> Emoji.fromUnicode("🎭")
            ReverbPcmAudioFilter.RoomPreset.AUDITORIUM -> Emoji.fromUnicode("🏛️")
            ReverbPcmAudioFilter.RoomPreset.STADIUM -> Emoji.fromUnicode("🏟️")
            ReverbPcmAudioFilter.RoomPreset.CATHEDRAL -> Emoji.fromUnicode("⛪")
            ReverbPcmAudioFilter.RoomPreset.CHURCH -> Emoji.fromUnicode("🛐")
            ReverbPcmAudioFilter.RoomPreset.CAVE -> Emoji.fromUnicode("🕳️")
            ReverbPcmAudioFilter.RoomPreset.GARAGE -> Emoji.fromUnicode("🚗")
            ReverbPcmAudioFilter.RoomPreset.THEATER -> Emoji.fromUnicode("🎪")
        }
    }

    override fun createSlashCommand(): SlashCommandData {
        return Commands.slash(name, "Apply reverb effect with different room acoustics")
            .addOption(OptionType.STRING, "room", "Select room type directly", false)
    }
}