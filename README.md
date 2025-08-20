# Beat Music
<!-- PROJECT LOGO -->
<br />
<p align="center">
  <a href="https://menhera-chan.in/support">
    <img src="https://images-ext-1.discordapp.net/external/6rS71_vVQkP70pjsCjIRH3TZ2CbU_QA3eRySRW8FDlo/https/cdn.discordapp.com/avatars/881050313870684180/9b5bab68426ef69b6ef7fbca14f64b42.webp" alt="Logo" width="80" height="80">
  </a>

<h3 align="center">Beat Music</h3>
</p>

<!-- TABLE OF CONTENTS -->
## Table of Content
* [About the Project](#about-the-project)
  * [Features](#features)
  * [Library Used](#library-used)
* [Getting Started](#getting-started)
  * [Prerequisite](#prerequisite)
  * [Setup](#setup)
  * [Starting the Bot](#starting-the-bot)
  * [Additional settings](#additional-settings)
* [Commands](#commands)
  * [Music Commands](#music-commands)
  * [Filter Commands](#filter-commands)
* [Contributing](#contributing)
* [LICENSE](#license)
* [Contact](#contact)


<!-- ABOUT THE PROJECT -->
## About The Project

[Beat Music](https://menhera-chan.in/support) is a powerful Discord music bot designed to enhance your server's audio experience. Built with modern technologies and slash commands, it provides high-quality music playback with advanced audio filtering capabilities.

### Features
* 🎵 **High-Quality Music Playback** - Stream music from various sources
* 🎧 **Advanced Audio Filters** - Multiple filters including Nightcore, 8D, Bass Boost, and more
* 🏛️ **Professional Reverb System** - 11 different room acoustics (Small Room, Concert Hall, Cathedral, etc.)
* 🎼 **Spotify Integration** - Full Spotify support for playlists and tracks
* 🔄 **Smart Queue Management** - Advanced queue system with shuffle and management
* 🤖 **Slash Commands** - Modern Discord interaction with intuitive commands
* 📊 **Audio Visualization** - Real-time audio processing and effects
* 🔁 **Autoplay & 24/7** - Continuous playback capabilities
* 🛡️ **Age-Restricted Content** - Safe content filtering options
* 💾 **Database Support** - MongoDB integration for persistence



### Library Used
* [JDA](https://github.com/DV8FromTheWorld/JDA) - Discord API wrapper
* [lavaplayer-fork](https://github.com/Walkyst/lavaplayer-fork) - Audio playback engine
* [lavadsp](https://github.com/natanbc/lavadsp) - Digital signal processing
* [lavadsp-extended](https://github.com/rohank05/lavadsp-extended) - Extended audio filters
* [jda-nas](https://github.com/sedmelluq/jda-nas) - Native audio system
* [jda-chewtils](https://github.com/Chew/JDA-Chewtils) - JDA utilities
* [Kotlin](https://kotlinlang.org/) - Modern programming language
* [Gradle](https://gradle.org/) - Build automation

<!-- GETTING STARTED -->
## Getting Started

### Prerequisite
1. Download [Java 21 or greater](https://adoptium.net/temurin/releases/)
2. Download the [Latest Release](https://github.com/rohank05/Beat-Music/releases)
3. A Discord Bot Token ([Create Bot Application](https://discord.com/developers/applications))

### Setup

1. **Extract the Release Files**
   ```
   Unzip the downloaded release file to your preferred directory
   ```

2. **Create Discord Bot Application**
   - Go to [Discord Developer Portal](https://discord.com/developers/applications)
   - Create a new application and name it
   - Navigate to the "Bot" section
   - Create a bot and copy the token

3. **Configure Environment**
   - Rename `Example.env` to `.env`
   - Add your bot token: `DISCORD_TOKEN=your_bot_token_here`
   - Configure other optional settings as needed

4. **Invite Bot to Server**
   - Go to OAuth2 > URL Generator in Discord Developer Portal
   - Select scopes: `bot`, `applications.commands`
   - Select permissions: `Send Messages`, `Use Slash Commands`, `Connect`, `Speak`, `Use Voice Activity`
   - Copy the generated URL and invite the bot to your server

### Starting the Bot

#### First Time Setup
1. Run `Register.bat` (Windows) or `./register.sh` (Linux/Mac) to register slash commands
2. Wait for the registration to complete (this may take a few minutes)
3. Run `Start_Bot.bat` (Windows) or `./start_bot.sh` (Linux/Mac)

#### Subsequent Starts
- Simply run `Start_Bot.bat` (Windows) or `./start_bot.sh` (Linux/Mac)

> **Note:** Command registration is only needed once or when commands are updated.

### Additional Settings

Beat Music supports several optional features that can be enabled through environment configuration:

#### 1. Age Restricted Content Support
```env
# Add to .env file
YOUTUBE_EMAIL=your_email@gmail.com
YOUTUBE_PASSWORD=your_password
```
Enable playback of age-restricted YouTube content by providing Gmail credentials with mature content access.

#### 2. Spotify Integration
```env
# Add to .env file  
SPOTIFY_CLIENT_ID=your_spotify_client_id
SPOTIFY_CLIENT_SECRET=your_spotify_client_secret
```
1. Visit [Spotify Developer Dashboard](https://developer.spotify.com/dashboard/applications)
2. Create a new application
3. Copy Client ID and Client Secret
4. Add them to your `.env` file

#### 3. Database Support (MongoDB)
```env
# Add to .env file
ENABLE_DB=true
MONGODB_URI=mongodb://localhost:27017/beatmusic
```
- Install and configure MongoDB
- Set `ENABLE_DB=true` to enable database features
- Provides persistent settings and user preferences

#### 4. Advanced Configuration
```env
# Guild-specific deployment (optional)
DISCORD_GUILD=your_guild_id_here

# Logging level
LOG_LEVEL=INFO

# Audio quality settings
AUDIO_QUALITY=HIGH
```

## Commands

### Music Commands
- `/play <query>` - Play music from YouTube, Spotify, or direct links
- `/skip` - Skip the current track
- `/stop` - Stop playback and clear queue
- `/pause` - Pause current track
- `/resume` - Resume paused track
- `/queue` - View current queue
- `/nowplaying` - Show currently playing track
- `/shuffle` - Shuffle the queue
- `/clear` - Clear the entire queue
- `/shift <position>` - Move to specific position in queue

### Filter Commands
- `/filter` - Open filter selection menu with options:
  - **Nightcore** - Higher pitch and tempo
  - **8D Audio** - Rotating audio effect  
  - **Vibrato** - Frequency modulation
  - **Tremolo** - Amplitude modulation
  - **Bass Boost** - Enhanced low frequencies
  - **Echo** - Echo/delay effect
  - **Reverb** - Basic reverb effect

- `/reverb [room]` - Advanced reverb with room acoustics:
  - **Small Room** - Intimate setting (30ms, 50% decay)
  - **Medium Room** - Standard room (50ms, 60% decay)
  - **Large Room** - Spacious environment (75ms, 70% decay)
  - **Concert Hall** - Professional venue (100ms, 80% decay)
  - **Auditorium** - Large auditorium (150ms, 75% decay)
  - **Stadium** - Massive stadium (250ms, 85% decay)
  - **Cathedral** - Grand cathedral (300ms, 90% decay)
  - **Church** - Church acoustics (200ms, 80% decay)
  - **Cave** - Natural cave reverb (400ms, 90% decay)
  - **Garage** - Garage-like space (40ms, 60% decay)
  - **Theater** - Theater acoustics (125ms, 70% decay)

> **Pro Tip:** Combine multiple filters for unique sound experiences! Each reverb room has different characteristics for size, decay, and wet level.

<!-- CONTRIBUTING -->
## Contributing

Contributions are what make the open source community such an amazing place to learn, inspire, and create. Any contributions you make are **greatly appreciated**.

### Development Setup
1. **Fork the Project**
2. **Clone your fork**
   ```bash
   git clone https://github.com/yourusername/Beat-Music.git
   cd Beat-Music
   ```
3. **Set up development environment**
   ```bash
   # Install dependencies
   ./gradlew build
   
   # Create your .env file
   cp Example.env .env
   # Add your development bot token
   ```
4. **Create your Feature Branch**
   ```bash
   git checkout -b feature/AmazingFeature
   ```
5. **Make your changes and test thoroughly**
6. **Commit your Changes**
   ```bash
   git commit -m 'Add some AmazingFeature'
   ```
7. **Push to the Branch**
   ```bash
   git push origin feature/AmazingFeature
   ```
8. **Open a Pull Request**

### Code Quality Standards
- Follow Kotlin coding conventions
- Write meaningful commit messages
- Test all audio filter changes thoroughly
- Update documentation for new features
- Ensure all existing tests pass

### Reporting Issues
- Use the issue tracker for bugs and feature requests
- Provide detailed reproduction steps
- Include system information and logs when relevant

<!-- LICENSE -->
## LICENSE
This project is licensed under the GNU General Public License v3.0. By downloading and using this software, you agree to comply with the terms specified in the [LICENSE](LICENSE) file.

**Important:** This is free and open-source software. See the LICENSE file for full terms and conditions.

<!-- CONTACT -->
## Contact

**Developer:** [Rohan Kumar](https://github.com/rohank05)  
**Email:** rohan.shuvam@gmail.com

**Support & Community:**
- [Discord Support Server](https://discord.com/invite/a4zkCjg)
- [GitHub Issues](https://github.com/rohank05/Beat-Music/issues)
- [GitHub Discussions](https://github.com/rohank05/Beat-Music/discussions)

**Project Links:**
- [GitHub Repository](https://github.com/rohank05/Beat-Music)
- [Latest Releases](https://github.com/rohank05/Beat-Music/releases)
- [Documentation Wiki](https://github.com/rohank05/Beat-Music/wiki)

---

<p align="center">
  <a href="https://github.com/rohank05/Beat-Music">
    <img src="https://img.shields.io/github/stars/rohank05/Beat-Music?style=social" alt="GitHub stars">
  </a>
  <a href="https://github.com/rohank05/Beat-Music/fork">
    <img src="https://img.shields.io/github/forks/rohank05/Beat-Music?style=social" alt="GitHub forks">
  </a>
  <a href="https://github.com/rohank05/Beat-Music/issues">
    <img src="https://img.shields.io/github/issues/rohank05/Beat-Music" alt="GitHub issues">
  </a>
  <a href="https://github.com/rohank05/Beat-Music/blob/main/LICENSE">
    <img src="https://img.shields.io/github/license/rohank05/Beat-Music" alt="License">
  </a>
</p>

<p align="center">
  Made with ❤️ for the Discord community
</p>


