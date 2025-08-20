package com.therohankumar.modules

import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager
import com.sedmelluq.discord.lavaplayer.track.playback.NonAllocatingAudioFrameBuffer
import com.therohankumar.ENV
import org.slf4j.LoggerFactory

object AudioPlayerManager {
    private val logger = LoggerFactory.getLogger(AudioPlayerManager::class.java)
    
    val audioPlayerManager = DefaultAudioPlayerManager().apply {
        // Configure frame buffer for better performance
        configuration.setFrameBufferFactory { bufferDuration, audioDataFormat, stopping ->
            NonAllocatingAudioFrameBuffer(bufferDuration, audioDataFormat, stopping)
        }
        this.configuration.isFilterHotSwapEnabled = true
        
        try {
            // Use YouTube source manager
            val youtubeSourceManager = YoutubeAudioSourceManager()
            
            // Note for future implementation: IP rotator functionality
            ENV.IPV6_BLOCK?.let { ipv6Block ->
                logger.info("IPv6 block configured: $ipv6Block")
                logger.info("IP rotator support would be enabled here when dependencies are available")
                // Future implementation would configure IP rotation here:
                // - Setup RotatingNanoIpRoutePlanner with IPv6 blocks
                // - Configure YoutubeIpRotatorSetup for the source manager
                // - This helps avoid YouTube rate limiting with multiple IP addresses
            }
            
            this.registerSourceManager(youtubeSourceManager)
            logger.info("YouTube source manager registered successfully" + 
                       if (ENV.IPV6_BLOCK != null) " (IPv6 block ready for rotator)" else "")
        } catch (ex: Exception) {
            logger.error("Failed to register YouTube source manager", ex)
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