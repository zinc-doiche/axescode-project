plugins {
    id("java")
}

group = "com.github.mckd"
version = "1.0"

dependencies {
    implementation(project(":axescode"))
    compileOnly ("org.projectlombok:lombok:1.18.28")
    annotationProcessor ("org.projectlombok:lombok:1.18.28")

    compileOnly ("com.github.MilkBowl:VaultAPI:1.7")
    compileOnly ("io.papermc.paper:paper-api:1.19.4-R0.1-SNAPSHOT")
    compileOnly ("com.github.LoneDev6:API-ItemsAdder:3.5.0b")
}

tasks.register("prepareKotlinBuildScriptModel"){}