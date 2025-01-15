package com.therohankumar

import ENV
import EventListeners
import modules.CommandManager
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.requests.GatewayIntent
import net.dv8tion.jda.api.utils.cache.CacheFlag

fun main() {
    CommandManager.registerAllCommand()
    val jda = JDABuilder.createDefault(ENV.DISCORD_TOKEN).enableIntents(GatewayIntent.GUILD_VOICE_STATES).enableCache(CacheFlag.VOICE_STATE).addEventListeners(EventListeners()).build()
    jda.awaitReady()
    jda.getGuildById(735899211677041099)?.updateCommands()?.addCommands(CommandManager.getAllSlashCommand())?.queue()
}