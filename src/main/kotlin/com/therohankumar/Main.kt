package com.therohankumar

import com.therohankumar.modules.CommandManager
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.requests.GatewayIntent
import net.dv8tion.jda.api.utils.cache.CacheFlag

fun main() {
    CommandManager.registerAllCommand()
    val jda = JDABuilder.createDefault(ENV.DISCORD_TOKEN).enableIntents(GatewayIntent.GUILD_VOICE_STATES).enableCache(CacheFlag.VOICE_STATE).addEventListeners(
        EventListeners(), AudioFilterSelectListeners()
    ).build()
    jda.awaitReady()
    jda.getGuildById(759181529753976863)?.updateCommands()?.addCommands(CommandManager.getAllSlashCommand())?.queue()
}