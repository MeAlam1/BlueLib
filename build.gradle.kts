plugins {
    alias(libs.plugins.minotaur) apply false
    alias(libs.plugins.curseforgegradle) apply false
    alias(libs.plugins.github.release) apply true

    // Required for NeoGradle
    alias(libs.plugins.ideaext)
}

val modId: String by project
val mcVersion = libs.versions.minecraft.asProvider().get()

githubRelease {
    token(System.getenv("GITHUB") ?: "Invalid/No API Token Found")
    owner.set("MeAlam1")
    repo.set("BlueLib")
    tagName.set("2.4.4")
    targetCommitish.set("1.21-1.21.3")
    releaseName.set("BlueLib 2.4.4")
    body.set(rootProject.file("changelog.md").readText())
    draft.set(false)
    prerelease.set(false)
    overwrite.set(true)
    releaseAssets.setFrom(
        rootProject.file("common/build/libs/${version}-common-${mcVersion}-${modId}-${version}.jar"),
        rootProject.file("fabric/build/libs/${version}-fabric-${mcVersion}-${modId}-${version}.jar"),
        rootProject.file("neoforge/build/libs/${version}-neoforge-${mcVersion}-${modId}-${version}.jar")
    )
}


tasks.register("publishAll") {
    group = "publishing"
    description = "Publishes all modules to Modrinth and CurseForge"
    dependsOn(
        ":common:publish",
        ":fabric:publish",
        ":neoforge:publish",
        "githubRelease"
    )
}