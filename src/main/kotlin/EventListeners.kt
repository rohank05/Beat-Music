import interfaces.ICommand
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import modules.CommandManager
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.events.session.ReadyEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import org.slf4j.LoggerFactory

class EventListeners: ListenerAdapter() {
    private val logger = LoggerFactory.getLogger(EventListeners::class.java)
    private val scope = CoroutineScope(Dispatchers.Default)
    override fun onReady(event: ReadyEvent) {
        logger.info("Bot is online")
    }

    override fun onSlashCommandInteraction(event: SlashCommandInteractionEvent) {
        val command: ICommand? = CommandManager.getCommand(event.name)
        scope.launch {
            command?.execute(event)
        }
    }

}