package commands

import interfaces.ICommand
import modules.AudioPlayerManager
import modules.EmbedUtils
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.commands.build.Commands
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData

class Stop: ICommand {
    override val name = "stop"
    override suspend fun execute(event: SlashCommandInteractionEvent) {
        event.deferReply().queue()
        if(!AudioPlayerManager.musicManagerExist(guildId = event.guild!!.idLong)) {
            val embed = EmbedUtils.createErrorEmbed("Not Playing Anything", "You need to start playing song first")
            event.hook.sendMessageEmbeds(embed).queue()
            return
        }
        val musicManager = AudioPlayerManager.getMusicManager(event.guild!!.idLong)
        musicManager.player.stopTrack()
        AudioPlayerManager.destroyMusicManager(event.guild!!.idLong)
        event.guild!!.audioManager.closeAudioConnection()
        val embed = EmbedUtils.createStopEmbed(event.user)
        event.hook.sendMessageEmbeds(embed).queue()
    }

    override fun createSlashCommand(): SlashCommandData {
        return Commands.slash("stop", "Stop the audio player and leave the channel")
    }
}