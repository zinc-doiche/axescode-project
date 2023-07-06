plugins {
    id("java-library")
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "com.github.axescode"
version = "1.0"

dependencies {
    implementation ("org.mybatis:mybatis:3.5.13")
    implementation ("mysql:mysql-connector-java:8.0.33")
    implementation ("com.zaxxer:HikariCP:5.0.1")

    compileOnly ("org.projectlombok:lombok:1.18.28")
    annotationProcessor ("org.projectlombok:lombok:1.18.28")

    compileOnly ("com.github.MilkBowl:VaultAPI:1.7")
    compileOnly ("io.papermc.paper:paper-api:1.19.4-R0.1-SNAPSHOT")
    compileOnly ("com.github.LoneDev6:API-ItemsAdder:3.5.0b")
}
