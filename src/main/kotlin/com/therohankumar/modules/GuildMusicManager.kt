package com.therohankumar.modules

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer

class GuildMusicManager(audioPlayer: AudioPlayer){
    val sendHandler: AudioPlayerSendHandler = AudioPlayerSendHandler(audioPlayer)
    val taskScheduler = TrackScheduler(audioPlayer)
    val audioFilter = Filters(audioPlayer)
    val player = audioPlayer
    init {
        audioPlayer.addListener(taskScheduler)
    }
}