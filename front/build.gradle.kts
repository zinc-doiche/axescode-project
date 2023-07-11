plugins {
    kotlin("jvm") version "1.8.21"
    application
}

group = "com.github.axessystem"
version = "1.0"

repositories {
    maven { url = uri("https://repo.bg-software.com/repository/api/") }
}

dependencies {
    implementation(project(":axescode"))
    implementation(project(":mckd"))
    //compileOnly("com.bgsoftware:SuperiorSkyblockAPI:2023.2")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.2")

    implementation("io.github.monun:invfx-api:3.3.0")
    implementation("io.github.monun:tap-api:4.9.6")
    implementation("io.github.monun:kommand-api:3.1.6")
    implementation("io.github.monun:heartbeat-coroutines:0.0.5")

    //compileOnly("com.github.MilkBowl:VaultAPI:1.7")
    compileOnly ("io.papermc.paper:paper-api:1.19.4-R0.1-SNAPSHOT")
    compileOnly ("com.github.LoneDev6:API-ItemsAdder:3.5.0b")
}