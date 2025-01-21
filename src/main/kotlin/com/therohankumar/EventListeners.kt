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
        // Get bot's voice state
        val botVoiceState = event.guild.selfMember.voiceState ?: return

        // Get bot's voice channel
        val botChannel = botVoiceState.channel ?: run {
            AudioPlayerManager.destroyMusicManager(event.guild.idLong)
            return
        }

        // If this is a voice update event, check both old and new channels
        if (event is GuildVoiceUpdateEvent) {
            // If someone left the bot's channel
            if (event.channelLeft == botChannel) {
                // Check if bot is alone now
                if (botChannel.members.size <= 1) {
                    event.guild.audioManager.closeAudioConnection()
                    AudioPlayerManager.destroyMusicManager(event.guild.idLong)
                }
            }
            return
        }

        // For other voice events, check if it's related to bot's channel
        if (event.voiceState.channel != botChannel) return

        // Check if bot is alone
        if (botChannel.members.size <= 1) {
            event.guild.audioManager.closeAudioConnection()
            AudioPlayerManager.destroyMusicManager(event.guild.idLong)
        }
    }
}