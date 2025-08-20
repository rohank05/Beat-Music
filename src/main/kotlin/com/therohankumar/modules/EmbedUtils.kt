package com.therohankumar.modules

import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.MessageEmbed
import net.dv8tion.jda.api.entities.User
import java.awt.Color

object EmbedUtils {
    val YELLOW_COLOR = Color(255, 255, 0)
    val GREEN_COLOR = Color(46, 204, 113)  
    val RED_COLOR = Color(231, 76, 60)
    val BLUE_COLOR = Color(52, 152, 219)
    val PURPLE_COLOR = Color(155, 89, 182)

    fun createNowPlayingEmbed(
        trackTitle: String,
        trackUrl: String,
        author: String,
        durationMillis: Long,
        thumbnail: String?,
        requestedBy: User
    ): MessageEmbed {
        return EmbedBuilder().apply {
            setColor(GREEN_COLOR)
            setTitle("🎵 Now Playing")
            setDescription(buildString {
                append("**[$trackTitle]($trackUrl)**\n")
                append("by $author\n")
                append("Duration: `${formatDuration(durationMillis)}`")
            })
            setThumbnail(thumbnail ?: getTrackThumbnail(trackUrl))
            setFooter("Requested by ${requestedBy.name}", requestedBy.effectiveAvatarUrl)
        }.build()
    }

    fun createAddedToQueueEmbed(
        trackTitle: String,
        trackUrl: String,
        author: String,
        durationMillis: Long,
        thumbnail: String?,
        requestedBy: User
    ): MessageEmbed {
        return EmbedBuilder().apply {
            setColor(BLUE_COLOR)
            setTitle("➕ Added to Queue")
            setDescription(buildString {
                append("**[$trackTitle]($trackUrl)**\n")
                append("by $author\n")
                append("Duration: `${formatDuration(durationMillis)}`")
            })
            setThumbnail(thumbnail ?: getTrackThumbnail(trackUrl))
            setFooter("Requested by ${requestedBy.name}", requestedBy.effectiveAvatarUrl)
        }.build()
    }

    fun createPlaylistAddedEmbed(
        playlistName: String,
        tracksAdded: Int,
        tracks: List<Triple<String, String, String>>, // List of (title, url, author)
        thumbnail: String?,
        firstTrackUrl: String,
        requestedBy: User
    ): MessageEmbed {
        return EmbedBuilder().apply {
            setColor(YELLOW_COLOR)
            setTitle("Added Playlist to Queue")
            setDescription(buildString {
                append("**$playlistName**\n\n")
                append("**Tracks Added:** $tracksAdded\n")
                if (tracksAdded > 100) {
                    append("_(Limited to first 100 tracks)_\n")
                }
                append("\n**First Few Tracks:**\n")
                tracks.take(3).forEach { (title, url, author) ->
                    append("• [$title]($url) by $author\n")
                }
                if (tracksAdded > 3) {
                    append("_...and ${tracksAdded - 3} more tracks_")
                }
            })
            setThumbnail(thumbnail ?: getTrackThumbnail(firstTrackUrl))
            setFooter("Requested by ${requestedBy.name}", requestedBy.effectiveAvatarUrl)
        }.build()
    }

    fun createLoopEmbed(mode: String, requestedBy: User): MessageEmbed {
        return EmbedBuilder().apply {
            setColor(YELLOW_COLOR)
            setTitle("Loop Mode Changed")
            setDescription("Loop mode set to: **$mode**")
            setFooter("Requested by ${requestedBy.name}", requestedBy.effectiveAvatarUrl)
        }.build()
    }

    fun createSkipEmbed(
        skippedTrack: String,
        nextTrack: String? = null,
        requestedBy: User
    ): MessageEmbed {
        return EmbedBuilder().apply {
            setColor(YELLOW_COLOR)
            setTitle("Track Skipped")
            setDescription(buildString {
                append("Skipped: **$skippedTrack**")
                if (nextTrack != null) {
                    append("\nNow playing: **$nextTrack**")
                }
            })
            setFooter("Requested by ${requestedBy.name}", requestedBy.effectiveAvatarUrl)
        }.build()
    }

    fun createStopEmbed(requestedBy: User): MessageEmbed {
        return EmbedBuilder().apply {
            setColor(YELLOW_COLOR)
            setTitle("Playback Stopped")
            setDescription("Music playback has been stopped and the queue has been cleared.")
            setFooter("Requested by ${requestedBy.name}", requestedBy.effectiveAvatarUrl)
        }.build()
    }

    fun createPauseEmbed(isPaused: Boolean, requestedBy: User): MessageEmbed {
        return EmbedBuilder().apply {
            setColor(YELLOW_COLOR)
            setTitle(if (isPaused) "Playback Paused" else "Playback Resumed")
            setDescription(if (isPaused)
                "The music has been paused. Use `/resume` to continue playback."
            else "The music has been resumed."
            )
            setFooter("Requested by ${requestedBy.name}", requestedBy.effectiveAvatarUrl)
        }.build()
    }

    fun createAudioFilterEmbed(
        filterName: String,
        isEnabled: Boolean,
        description: String? = null,
        requestedBy: User
    ): MessageEmbed {
        return EmbedBuilder().apply {
            setColor(YELLOW_COLOR)
            setTitle("Audio Filter: $filterName")
            setDescription(buildString {
                append("Filter has been **${if (isEnabled) "enabled" else "disabled"}**")
                if (description != null) {
                    append("\n\n$description")
                }
            })
            setFooter("Requested by ${requestedBy.name}", requestedBy.effectiveAvatarUrl)
        }.build()
    }

    fun createErrorEmbed(
        title: String,
        description: String,
        requestedBy: User? = null
    ): MessageEmbed {
        return EmbedBuilder().apply {
            setColor(RED_COLOR)
            setTitle("⚠️ $title")
            setDescription(description)
            if (requestedBy != null) {
                setFooter("Requested by ${requestedBy.name}", requestedBy.effectiveAvatarUrl)
            }
        }.build()
    }

    fun createQueueEmbed(
        queueDescription: String,
        totalTracks: Int,
        totalDuration: Long,
        currentPage: Int,
        totalPages: Int,
        requestedBy: User
    ): MessageEmbed {
        return EmbedBuilder().apply {
            setColor(YELLOW_COLOR)
            setTitle("Queue - Page $currentPage/$totalPages")
            setDescription(queueDescription)
            addField(
                "Queue Info",
                "**$totalTracks** tracks | Total Duration: `${formatDuration(totalDuration)}`",
                false
            )
            setFooter("Requested by ${requestedBy.name}", requestedBy.effectiveAvatarUrl)
        }.build()
    }
    fun createGenericEmbed(
        title: String,
        description: String,
        requestedBy: User
    ): MessageEmbed{
        return EmbedBuilder().apply {
            setColor(YELLOW_COLOR)
            setTitle(title)
            setDescription(description)
            setFooter("Requested by ${requestedBy.name}", requestedBy.effectiveAvatarUrl)
        }.build()
    }

    private fun formatDuration(milliseconds: Long): String {
        val seconds = milliseconds / 1000
        val hours = seconds / 3600
        val minutes = (seconds % 3600) / 60
        val remainingSeconds = seconds % 60

        return when {
            hours > 0 -> String.format("%d:%02d:%02d", hours, minutes, remainingSeconds)
            else -> String.format("%02d:%02d", minutes, remainingSeconds)
        }
    }

    private fun getTrackThumbnail(url: String): String? {
        return try {
            // Extract video ID from YouTube URL
            val videoId = when {
                url.contains("youtu.be/") -> {
                    val parts = url.split("youtu.be/")
                    if (parts.size > 1) parts[1].split("?")[0].take(11) else null
                }
                url.contains("youtube.com/watch?v=") -> {
                    val parts = url.split("watch?v=")
                    if (parts.size > 1) parts[1].split("&")[0].take(11) else null
                }
                url.contains("youtube.com/watch") && url.contains("v=") -> {
                    val regex = Regex("v=([a-zA-Z0-9_-]{11})")
                    regex.find(url)?.groupValues?.get(1)
                }
                else -> null
            }
            
            if (videoId != null && videoId.length == 11) {
                "https://img.youtube.com/vi/$videoId/mqdefault.jpg"
            } else null
        } catch (e: Exception) {
            null
        }
    }
}