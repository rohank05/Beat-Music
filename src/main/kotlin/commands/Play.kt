package commands

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist
import com.sedmelluq.discord.lavaplayer.track.AudioTrack
import interfaces.ICommand
import modules.AudioPlayerManager
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.Commands
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData

class Play: ICommand {
    override val name = "play"

    override suspend fun execute(event: SlashCommandInteractionEvent) {
        event.deferReply(true).queue()
        if(event.guild == null) return
        val query = event.interaction.getOption("query")!!.asString
        val musicManager = AudioPlayerManager.getMusicManager(event.guild!!.idLong)
        if(ensureVoiceChannel(event, musicManager.audioPlayer)) {
            event.guild!!.audioManager.sendingHandler = musicManager.sendHandler
            AudioPlayerManager.audioPlayerManager.loadItem("ytmsearch:${query}", Loader(event, musicManager.audioPlayer, query))
        }

    }

    fun ensureVoiceChannel(event: SlashCommandInteractionEvent, player: AudioPlayer): Boolean {
        val ourVC = event.guild!!.selfMember.voiceState?.channel
        val theirVC = event.member!!.voiceState?.channel
        if (ourVC == null && theirVC == null) {
            event.hook.sendMessage("You need to be in voice channel").queue()
            return false
        }
        if (ourVC !== theirVC && theirVC !== null) {
           val canTalk = event.guild!!.selfMember.hasPermission(Permission.VOICE_SPEAK, Permission.VOICE_CONNECT)
            if(!canTalk) {
                event.hook.sendMessage("I need permission to connect and speak in ${theirVC.name}").queue()
                return false
            }
            event.guild!!.audioManager.openAudioConnection(theirVC)
            return true
        }
        return true
    }

    inner class Loader (private val event: SlashCommandInteractionEvent, private val player: AudioPlayer, private val identified: String): AudioLoadResultHandler {
        override fun trackLoaded(track: AudioTrack) {
            player.startTrack(track, false)
        }

        override fun playlistLoaded(tracks: AudioPlaylist) {
            player.startTrack(tracks.tracks[0], false)
        }

        override fun noMatches() {

        }

        override fun loadFailed(p0: FriendlyException?) {

        }

    }

    override fun createSlashCommand(): SlashCommandData {
        return Commands.slash("play", "Search and Play/Queue Song or Playlist").addOption(OptionType.STRING, "query", "Name or URL", true)
    }
}