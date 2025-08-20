package com.therohankumar

import io.github.cdimascio.dotenv.dotenv

object ENV {
    private val dotenv = dotenv {
        ignoreIfMalformed = true
        ignoreIfMissing = true
    }
    
    val DISCORD_TOKEN: String? = dotenv["DISCORD_TOKEN"]?.takeIf { it.isNotBlank() }
    val DISCORD_GUILD: String? = dotenv["DISCORD_GUILD"]?.takeIf { it.isNotBlank() }
    val IPV6_BLOCK: String? = dotenv["IPV6_BLOCK"]?.takeIf { it.isNotBlank() }
    
    // Spotify configuration
    val SPOTIFY_CLIENT_ID: String? = dotenv["SPOTIFY_CLIENT_ID"]?.takeIf { it.isNotBlank() }
    val SPOTIFY_CLIENT_SECRET: String? = dotenv["SPOTIFY_CLIENT_SECRET"]?.takeIf { it.isNotBlank() }
    
    // YouTube configuration  
    val YOUTUBE_EMAIL: String? = dotenv["YOUTUBE_EMAIL"]?.takeIf { it.isNotBlank() }
    val YOUTUBE_PASSWORD: String? = dotenv["YOUTUBE_PASSWORD"]?.takeIf { it.isNotBlank() }
    
    // Database configuration
    val ENABLE_DB: Boolean = dotenv["ENABLE_DB"]?.toBoolean() ?: false
    val MONGODB_URI: String? = dotenv["MONGODB_URI"]?.takeIf { it.isNotBlank() }
    
    // Logging configuration
    val LOG_LEVEL: String = dotenv["LOG_LEVEL"] ?: "INFO"
    
    // Audio quality configuration
    val AUDIO_QUALITY: String = dotenv["AUDIO_QUALITY"] ?: "HIGH"
}