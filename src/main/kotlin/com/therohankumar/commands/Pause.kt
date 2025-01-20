package com.therohankumar.commands

import com.therohankumar.interfaces.ICommand
import com.therohankumar.modules.AudioPlayerManager
import com.therohankumar.modules.EmbedUtils
import com.therohankumar.modules.Utilities
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.commands.build.Commands
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData

class Pause: ICommand {
    override val name = "pause"
    override suspend fun execute(event: SlashCommandInteractionEvent) {
        if(!Utilities.commandCheck(event)) return
        val musicManager = AudioPlayerManager.getMusicManager(event.guild!!.idLong)
        if(musicManager.player.isPaused) {
            val embed = EmbedUtils.createErrorEmbed("Error", "Player is already paused")
            event.hook.sendMessageEmbeds(embed).queue()
            return
        }
        musicManager.player.isPaused = true
        val embed = EmbedUtils.createPauseEmbed(true, event.user)
        event.hook.sendMessageEmbeds(embed)
    }
    override fun createSlashCommand(): SlashCommandData {
        return Commands.slash("pause", "Pause Audio")
    }
}