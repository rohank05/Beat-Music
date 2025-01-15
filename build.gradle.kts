plugins {
    kotlin("jvm") version "2.0.21"
}

group = "com.therohankumar"
version = "2.0"

repositories {
    mavenCentral()
    maven("https://m2.dv8tion.net/releases")
    maven("https://maven.arbjerg.dev/releases")
    maven("https://jitpack.io")
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("net.dv8tion:JDA:5.2.2")
    implementation("dev.arbjerg:lavaplayer:2.2.2")
    implementation("dev.arbjerg:lavadsp:0.7.8")
    implementation("io.github.cdimascio:dotenv-kotlin:6.4.1")
    implementation("ch.qos.logback:logback-classic:1.5.16")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    implementation("dev.lavalink.youtube:common:1.11.3")
    implementation("dev.arbjerg:lavaplayer-ext-youtube-rotator:2.2.2")
    testImplementation(kotlin("test"))

}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}