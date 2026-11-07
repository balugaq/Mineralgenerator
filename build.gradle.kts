plugins {
    java
    alias(libs.plugins.shadow)
}

group = "io.github.shixinzia"
version = "1.0.0"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://jitpack.io")
}

dependencies {
    // Core
    compileOnly(libs.spigot.api)
    compileOnly(libs.slimefun4)

    // Shaded
    implementation(libs.infinitylib)
    implementation(libs.guizhanlib)

    // Tools
    compileOnly(libs.jsr305)
    compileOnly(libs.lombok)
    annotationProcessor(libs.lombok)
}

tasks {
    compileJava {
        options.encoding = "UTF-8"
        options.release = 21
    }

    processResources {
        filesMatching("plugin.yml") {
            expand("version" to project.version.toString())
        }
    }

    shadowJar {
        archiveBaseName.set("Mineralgenerator")
        archiveVersion.set(project.version.toString())
        archiveClassifier.set("")

        minimize()

        relocate("io.github.mooy1.infinitylib", "io.github.shixinzia.mineralgenerator.infinitylib")
        relocate("net.guizhanss.guizhanlib", "io.github.shixinzia.mineralgenerator.guizhanlib")

        exclude("META-INF/*")
    }

    build {
        dependsOn(shadowJar)
    }
}

defaultTasks("clean", "build")
