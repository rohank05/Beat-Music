package com.therohankumar.modules

import com.therohankumar.ENV
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import org.slf4j.LoggerFactory
import java.io.File
import java.nio.file.Paths
import kotlin.system.exitProcess

object Utilities {
    private val logger = LoggerFactory.getLogger(Utilities::class.java)
    
    fun commandCheck(event: SlashCommandInteractionEvent): Boolean {
        event.deferReply().queue()
        
        val guildId = event.guild?.idLong
        if (guildId == null) {
            logger.warn("Command executed outside of guild context")
            return false
        }
        
        if (!AudioPlayerManager.musicManagerExist(guildId)) {
            val embed = EmbedUtils.createErrorEmbed(
                "No Music Playing", 
                "You need to start playing a song first. Use `/play <song>` to get started!"
            )
            event.hook.sendMessageEmbeds(embed).queue()
            return false
        }
        
        val userVoiceChannel = event.member?.voiceState?.channel
        val botVoiceChannel = event.guild?.selfMember?.voiceState?.channel
        
        if (userVoiceChannel != botVoiceChannel) {
            val embed = EmbedUtils.createErrorEmbed(
                "Voice Channel Mismatch",
                "You need to be in the same voice channel as the bot to use this command!"
            )
            event.hook.sendMessageEmbeds(embed).queue()
            return false
        }
        
        return true
    }
    
    // ENV check to make sure it exists and is not blank
    fun envCheck() {
        val envFilePath = File(Paths.get("").toAbsolutePath().toString() + "/.env")
        
        if (!envFilePath.exists()) {
            logger.error("Missing .env file")
            logger.error("Expected .env path: ${envFilePath.absolutePath}")
            logger.error("Please create a .env file with your Discord bot token")
            exitProcess(1)
        }
        
        if (ENV.DISCORD_TOKEN.isNullOrBlank()) {
            logger.error("Empty or missing Discord token")
            logger.error("Please add DISCORD_TOKEN=your_bot_token to your .env file")
            exitProcess(1)
        }
        
        logger.info("Environment configuration loaded successfully")
    }
}