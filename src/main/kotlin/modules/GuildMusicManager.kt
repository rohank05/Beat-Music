package modules

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer

class GuildMusicManager(val audioPlayer: AudioPlayer){
    val sendHandler: AudioPlayerSendHandler = AudioPlayerSendHandler(audioPlayer)
}