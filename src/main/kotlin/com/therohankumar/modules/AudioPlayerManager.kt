package com.therohankumar.modules

import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers
import com.therohankumar.ENV
import org.slf4j.LoggerFactory

object AudioPlayerManager {
    private val logger = LoggerFactory.getLogger(AudioPlayerManager::class.java)
    
    val audioPlayerManager = DefaultAudioPlayerManager().apply {
        this.configuration.isFilterHotSwapEnabled = true
        
        // Register standard audio sources
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