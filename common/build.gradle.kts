import net.darkhax.curseforgegradle.TaskPublishCurseForge

plugins {
    id("bluelib-convention")
    alias(libs.plugins.minotaur)
    alias(libs.plugins.curseforgegradle)
    alias(libs.plugins.moddevgradle)
    alias(libs.plugins.com.diffplug.spotless)
    alias(libs.plugins.com.github.hierynomus.license)
}

val modId: String by project
val mcVersion = libs.versions.minecraft.asProvider().get()
val bluelibVersion = libs.versions.bluelib.get()

if (bluelibVersion.isEmpty()) {
    throw GradleException("libs.versions.bluelib is empty. Please set a valid version in your version catalog.")
}

version = bluelibVersion

base {
    archivesName.set("${version}-common-${mcVersion}-${modId}")
}

repositories {
    maven(url = "${rootProject.projectDir}/deps")
    maven("https://maven.blamejared.com/")
}

neoForge {
    neoFormVersion = libs.versions.neoform.get()
    validateAccessTransformers.set(true)
    accessTransformers.files.setFrom("src/main/resources/META-INF/accesstransformer-nf.cfg")

    parchment.minecraftVersion.set(libs.versions.parchment.minecraft.get())
    parchment.mappingsVersion.set(libs.versions.parchment.asProvider().get())
}

dependencies {
    compileOnly(libs.mixin)
    compileOnly(libs.mixinextras.common)
    compileOnlyApi(libs.jei.api)
}

modrinth {
    token.set(System.getenv("MODRINTH") ?: "Invalid/No API Token Found")
    projectId.set("ZrEU4mLQ")
    versionNumber.set(version.toString())
    versionName.set("${version}-common-${mcVersion}-${modId}")
    uploadFile.set(tasks.named<Jar>("jar"))
    changelog.set(rootProject.file("changelog.md").readText(Charsets.UTF_8))
    gameVersions.set(listOf(mcVersion, "1.21.2", "1.21.3"))
    versionType.set("release")
    loaders.set(listOf("neoforge", "forge"))
    dependencies {
        required.project("bluelib")
        optional.project("jei")
    }

    //debugMode = true
    //https://github.com/modrinth/minotaur#available-properties
}

tasks.register<TaskPublishCurseForge>("publishToCurseForge") {
    group = "publishing"
    apiToken = System.getenv("CURSEFORGE") ?: "Invalid/No API Token Found"

    val mainFile = upload(1132979, tasks.jar)
    mainFile.displayName = "${version}-common-${mcVersion}-${modId}"
    mainFile.releaseType = "release"
    mainFile.addModLoader("NeoForge", "Fabric", "Forge")
    mainFile.addGameVersion(mcVersion, "1.21.2", "1.21.3")
    mainFile.addJavaVersion("Java 21")
    mainFile.changelog = rootProject.file("changelog.md").readText(Charsets.UTF_8)

    //debugMode = true
    //https://github.com/Darkhax/CurseForgeGradle#available-properties
}

publishing {
    publications {
        create<MavenPublication>("bluelib") {
            from(components["java"])
            artifactId = base.archivesName.get()
        }
    }
}

tasks.named<DefaultTask>("publish").configure {
    finalizedBy("modrinth")
    finalizedBy("publishToCurseForge")
}


spotless {
    java {
        leadingSpacesToTabs()
        endWithNewline()
        removeUnusedImports()
        toggleOffOn()

        // Pin version to 4.31 due to Spotless bug https://github.com/diffplug/spotless/issues/1992
        eclipse("4.31").configFile(rootProject.file("codeformat/formatter-config.xml"))

        importOrder()

        bumpThisNumberIfACustomStepChanges(3)
    }
}

license {
    header = rootProject.file("HEADER")
    include("**/*.java")
    strictCheck = true

    mapping("java", "SLASHSTAR_STYLE")

    skipExistingHeaders = false
}


