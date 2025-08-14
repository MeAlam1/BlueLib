plugins {
    alias(libs.plugins.minotaur) apply false
    alias(libs.plugins.curseforgegradle) apply false

    // Required for NeoGradle
    alias(libs.plugins.ideaext)
}

tasks.register("publishAll") {
    group = "publishing"
    description = "Publishes all modules to Modrinth and CurseForge"
    dependsOn(
        ":common:publish",
        ":fabric:publish",
        ":neoforge:publish"
    )
}