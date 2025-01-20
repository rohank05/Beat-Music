package com.therohankumar.modules

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent

object Utilities {
    fun commandCheck(event: SlashCommandInteractionEvent): Boolean {
        event.deferReply().queue()
        if(!AudioPlayerManager.musicManagerExist(guildId = event.guild!!.idLong)) {
            val embed = EmbedUtils.createErrorEmbed("Not Playing Anything", "You need to start playing song first")
            event.hook.sendMessageEmbeds(embed).queue()
            return false
        }
        return true
    }
}