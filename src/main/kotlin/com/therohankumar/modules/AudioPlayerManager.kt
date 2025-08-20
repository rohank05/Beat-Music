package com.therohankumar.modules

import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager
import com.therohankumar.ENV
import org.slf4j.LoggerFactory

object AudioPlayerManager {
    private val logger = LoggerFactory.getLogger(AudioPlayerManager::class.java)
    
    val audioPlayerManager = DefaultAudioPlayerManager().apply {
        this.configuration.isFilterHotSwapEnabled = true
        
        try {
            // Try to set up YouTube source manager with available dependencies
            val youtubeSourceManager = YoutubeAudioSourceManager()
            this.registerSourceManager(youtubeSourceManager)
            logger.info("YouTube source manager registered successfully")
        } catch (ex: Exception) {
            logger.warn("Failed to register YouTube source manager, using fallback sources: ${ex.message}")
        }
        
        // Register standard audio sources (includes HTTP, SoundCloud, etc.)
        AudioSourceManagers.registerRemoteSources(this)
        AudioSourceManagers.registerLocalSource(this)
        
        logger.info("Audio player manager initialized with filter hot-swap enabled")
    }
    
    private val musicManagers = mutableMapOf<Long, GuildMusicManager>()

    fun getMusicManager(guildId: Long): GuildMusicManager {
        return musicManagers.getOrPut(guildId) {
            logger.debug("Creating new music manager for guild: $guildId")
            GuildMusicManager(audioPlayerManager.createPlayer())
        }
    }

    fun destroyMusicManager(guildId: Long) {
        val manager = musicManagers[guildId]
        if (manager != null) {
            logger.debug("Destroying music manager for guild: $guildId")
            manager.player.destroy()
            musicManagers.remove(guildId)
        }
    }

    fun musicManagerExist(guildId: Long): Boolean {
        return musicManagers.containsKey(guildId)
    }

    fun init() {
        logger.info("AudioPlayerManager initialization complete")
    }
}