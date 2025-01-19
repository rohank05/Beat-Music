package modules

import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.MessageEmbed
import java.awt.Color

object Utilities {
    fun createYellowEmbed(title: String?, description: String?, url: String?, thumbnailUrl: String?): MessageEmbed {
        val embedBuilder = EmbedBuilder()
        if(title !== null) embedBuilder.setTitle(title)
        if(description !== null) embedBuilder.setDescription(title)
        if(url !== null) embedBuilder.setUrl(url)
        if(thumbnailUrl !== null) embedBuilder.setThumbnail(thumbnailUrl)
        embedBuilder.setColor((Color.YELLOW))
        return embedBuilder.build()
    }
}