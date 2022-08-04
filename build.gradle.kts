import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    java
    id("com.github.johnrengelman.shadow") version "7.1.2"
    id("net.minecrell.plugin-yml.bukkit") version "0.5.1"
    id("xyz.jpenilla.run-paper") version "1.0.6"
}

group = "net.osnixer"
version = "1.0.0"

repositories {
    gradlePluginPortal()
    mavenCentral()
    mavenLocal()

    maven { url = uri("https://repo.extendedclip.com/content/repositories/placeholderapi/") }
    maven { url = uri("https://papermc.io/repo/repository/maven-public/")}
    maven { url = uri("https://repo.panda-lang.org/releases") }
    maven { url = uri("https://maven.enginehub.org/repo") }
    maven { url = uri("https://repo.eternalcode.pl/releases") }
}


dependencies {
    // Spigot api
    compileOnly("org.spigotmc:spigot-api:1.18.2-R0.1-SNAPSHOT")

    implementation("dev.rollczi.litecommands:bukkit-adventure:2.3.4")
    implementation("net.kyori:adventure-platform-bukkit:4.1.1")
    implementation("net.kyori:adventure-text-minimessage:4.11.0")

    // cdn configs
    implementation("net.dzikoysk:cdn:1.13.23")

    // HikariCP
    implementation("com.zaxxer:HikariCP:4.0.3")

    // ormlite jdbc
    implementation("com.j256.ormlite:ormlite-jdbc:6.1")

    // triumphGui
    implementation("dev.triumphteam:triumph-gui:3.1.3")

    // bStats
    implementation("org.bstats:bstats-bukkit:3.0.0")
}

bukkit {
    main = "net.osnixer.kits.Kits"
    apiVersion = "1.13"
    prefix = "xrKits"
    authors = listOf("Osnixer", "Rollczi")
    name = "xrKits"
    version = "${project.version}"
}

tasks {
    runServer {
        minecraftVersion("1.19")
    }
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}


tasks.withType<ShadowJar> {
    archiveFileName.set("xrKits v${project.version}.jar")

    exclude("org/intellij/lang/annotations/**")
    exclude("org/jetbrains/annotations/**")
    exclude("org/checkerframework/**")
    exclude("META-INF/**")
    exclude("javax/**")


    relocate("panda", "net.osnixer.kits.shared.org.panda")
    relocate("dev.rollczi", "net.osnixer.kits.shared.dev.rollczi")
    relocate("org.panda_lang", "net.osnixer.kits.shared.org.panda")
    relocate("net.dzikoysk", "net.osnixer.kits.shared.net.dzikoysk")
    relocate("com.j256", "net.osnixer.kits.shared.com.j256")
    relocate("dev.triumphteam", "net.osnixer.kits.shared.dev.triumphteam")
    relocate("net.kyori", "net.osnixer.kits.shared.net.kyori")
    relocate("com.google.gson", "net.osnixer.crafthype.kits.shared.com.google.gson")
    relocate("org.bstats", "net.osnixer.kits.shared.org.bstats")
}