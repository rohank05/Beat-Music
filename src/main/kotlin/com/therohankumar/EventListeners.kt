package com.therohankumar

import com.therohankumar.interfaces.ICommand
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.therohankumar.modules.AudioPlayerManager
import com.therohankumar.modules.CommandManager
import net.dv8tion.jda.api.events.guild.voice.GenericGuildVoiceEvent
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceUpdateEvent
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.events.session.ReadyEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import org.slf4j.LoggerFactory

class EventListeners: ListenerAdapter() {
    private val logger = LoggerFactory.getLogger(EventListeners::class.java)
    private val scope = CoroutineScope(Dispatchers.Default)
    override fun onReady(event: ReadyEvent) {
        logger.info("Bot is online")
    }

    override fun onSlashCommandInteraction(event: SlashCommandInteractionEvent) {
        println(event.jda.guilds.size)
        if(event.guild === null) return
        val command: ICommand? = CommandManager.getCommand(event.name)
        scope.launch {
            command?.execute(event)
        }
    }

        override fun onGenericGuildVoice(event: GenericGuildVoiceEvent) {
            // Case 1: Bot is forcefully disconnected
            if (event is GuildVoiceUpdateEvent &&
                event.member.idLong == event.guild.selfMember.idLong &&
                event.channelLeft != null &&
                event.channelJoined == null) {
                AudioPlayerManager.destroyMusicManager(event.guild.idLong)
                return
            }

            // Return if bot is not in any voice channel
            val botVoiceState = event.guild.selfMember.voiceState ?: return

            // Return if bot is not connected to a voice channel
            val botChannel = botVoiceState.channel ?: run {
                AudioPlayerManager.destroyMusicManager(event.guild.idLong)
                return
            }

            // Return if the event is not from bot's current channel
            if (event.voiceState.channel != botChannel) return

            // Case 2: Everyone left the channel (only bot remains)
            if (botChannel.members.size <= 1) {
                val musicManager = AudioPlayerManager.getMusicManager(event.guild.idLong)
                musicManager.player.destroy()
                event.guild.audioManager.closeAudioConnection()
                AudioPlayerManager.destroyMusicManager(event.guild.idLong)
            }
    }
}