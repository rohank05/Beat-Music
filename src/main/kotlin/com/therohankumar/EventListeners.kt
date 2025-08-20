package com.therohankumar

import com.therohankumar.interfaces.ICommand
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.therohankumar.modules.AudioPlayerManager
import com.therohankumar.modules.CommandManager
import net.dv8tion.jda.api.OnlineStatus
import net.dv8tion.jda.api.entities.Activity
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceUpdateEvent
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.events.session.ReadyEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import org.slf4j.LoggerFactory

class EventListeners: ListenerAdapter() {
    private val logger = LoggerFactory.getLogger(EventListeners::class.java)
    private val scope = CoroutineScope(Dispatchers.Default)
    override fun onReady(event: ReadyEvent) {
        logger.info("Bot is online and ready to serve music!")
        logger.info("Connected to ${event.jda.guilds.size} guilds")
        event.jda.presence.setPresence(OnlineStatus.ONLINE, Activity.listening("/play • Enhanced Audio Experience"))
    }

    override fun onSlashCommandInteraction(event: SlashCommandInteractionEvent) {
        logger.debug("Processing command: ${event.name} from guild: ${event.guild?.name ?: "Unknown"}")
        
        if (event.guild == null) {
            logger.warn("Received command from non-guild context")
            return
        }
        
        val command: ICommand? = CommandManager.getCommand(event.name)
        if (command == null) {
            logger.warn("Unknown command received: ${event.name}")
            return
        }
        
        scope.launch {
            try {
                command.execute(event)
                logger.debug("Successfully executed command: ${event.name}")
            } catch (e: Exception) {
                logger.error("Error executing command ${event.name}: ${e.message}", e)
                if (!event.isAcknowledged) {
                    event.reply("❌ An error occurred while processing your command. Please try again later.")
                        .setEphemeral(true).queue()
                }
            }
        }
    }

    override fun onGuildVoiceUpdate(event: GuildVoiceUpdateEvent) {
        val voiceState = event.guild.selfMember.voiceState
        
        // Check if bot is in a voice channel
        if (voiceState?.channel == null) return
        
        // Check if bot was left alone in the channel
        val botChannel = voiceState.channel
        if (botChannel == event.channelLeft && botChannel.members.size == 1) {
            logger.info("Bot left alone in voice channel ${botChannel.name}, disconnecting...")
            event.guild.audioManager.closeAudioConnection()
            AudioPlayerManager.destroyMusicManager(event.guild.idLong)
        }
    }
}