package modules

import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers
import com.sedmelluq.discord.lavaplayer.track.playback.NonAllocatingAudioFrameBuffer

object AudioPlayerManager {
    val audioPlayerManager = DefaultAudioPlayerManager().apply {
        configuration.setFrameBufferFactory { i, audioDataFormat, atomicBoolean ->
            NonAllocatingAudioFrameBuffer(i, audioDataFormat, atomicBoolean)
        }
        this.configuration.isFilterHotSwapEnabled = true
        val ytSourceManager = dev.lavalink.youtube.YoutubeAudioSourceManager(true)
        ytSourceManager.useOauth2(null, false)
        this.registerSourceManagers(ytSourceManager)
        AudioSourceManagers.registerRemoteSources(this)
        AudioSourceManagers.registerLocalSource(this)
    }
    private val musicManagers = mutableMapOf<Long, GuildMusicManager>()

    fun getMusicManager(guildId: Long): GuildMusicManager {
        return musicManagers.getOrPut(guildId) {
            GuildMusicManager(audioPlayerManager.createPlayer())
        }
    }

    fun destroyMusicManager(guidlId: Long) {
        val manager = musicManagers[guidlId]
        manager?.player?.destroy()
        musicManagers.remove(guidlId)

    }

    fun musicManagerExist(guildId: Long) : Boolean {
        return musicManagers.containsKey(guildId)
    }
}