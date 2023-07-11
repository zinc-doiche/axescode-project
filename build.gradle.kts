subprojects {
    group = "com.github"
    version = "1.0"

    repositories {
        mavenCentral()
        mavenLocal()
        maven {
            name = "papermc-repo"
            url = uri("https://repo.papermc.io/repository/maven-public/")
        }
        maven {
            name = "sonatype"
            url = uri("https://oss.sonatype.org/content/groups/public/")
        }
        maven { url = uri("https://jitpack.io") }
    }
}
