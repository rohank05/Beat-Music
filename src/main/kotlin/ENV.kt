import io.github.cdimascio.dotenv.dotenv

object ENV {
    private val dotenv = dotenv()
    val DISCORD_TOKEN: String = dotenv["DISCORD_TOKEN"]
}