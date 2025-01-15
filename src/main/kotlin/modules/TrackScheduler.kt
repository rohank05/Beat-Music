package modules

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter
import com.sedmelluq.discord.lavaplayer.track.AudioTrack
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason
import java.util.concurrent.BlockingQueue
import java.util.concurrent.LinkedBlockingQueue

class TrackScheduler(private val audioPlayer: AudioPlayer): AudioEventAdapter() {
    val queue: BlockingQueue<AudioTrack> = LinkedBlockingQueue()
    var isRepeatig = false

    fun queue(track: AudioTrack) {
        if(audioPlayer.startTrack(track, true)) {
            queue.offer(track)
        }
    }

    fun nextTrack() {
        audioPlayer.startTrack(queue.poll(), false)
    }

    override fun onTrackEnd(player: AudioPlayer, track: AudioTrack, endReason: AudioTrackEndReason) {
        if(endReason.mayStartNext) {
            if(isRepeatig) {
                audioPlayer.startTrack(track.makeClone(), false)
            }
            nextTrack()
        }
    }
}