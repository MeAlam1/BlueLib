import net.darkhax.curseforgegradle.TaskPublishCurseForge
import net.fabricmc.loom.task.RemapJarTask

plugins {
    id("bluelib-convention")

    alias(libs.plugins.minotaur)
    alias(libs.plugins.curseforgegradle)
    alias(libs.plugins.loom)
    alias(libs.plugins.com.diffplug.spotless)
    alias(libs.plugins.com.github.hierynomus.license)
}

val modId: String by project
val mcVersion = libs.versions.minecraft.asProvider().get()
val parchmentMcVersion = libs.versions.parchment.minecraft.get()
val parchmentVersion = libs.versions.parchment.asProvider().get()
val bluelibVersion = libs.versions.bluelib.get()

if (bluelibVersion.isBlank()) {
    throw GradleException("libs.versions.bluelib is blank. Please set a valid version in your version catalog.")
}

version = bluelibVersion

base {
    archivesName = "${version}-fabric-${mcVersion}-${modId}"
}

repositories {
    maven {
        name = "ParchmentMC"
        url = uri("https://maven.parchmentmc.org")
        content {
            includeGroupAndSubgroups("org.parchmentmc")
        }
    }
    maven(url = "${rootProject.projectDir}/deps")
    maven("https://maven.blamejared.com/")
}

dependencies {
    minecraft(libs.minecraft)
    mappings(loom.layered() {
        officialMojangMappings()
        parchment("org.parchmentmc.data:parchment-${parchmentMcVersion}:${parchmentVersion}@zip")
    })
    modImplementation(libs.fabric)
    modImplementation(libs.fabric.api)
    compileOnly(project(":common"))
    modCompileOnlyApi(libs.jei.api)
    modRuntimeOnly(libs.jei.fabric)
}

loom {
    accessWidenerPath = file("src/main/resources/${modId}.accesswidener")

    mixin.defaultRefmapName.set("${modId}.refmap.json")

    runs {
        named("client") {
            configName = "Fabric Client"

            client()
            ideConfigGenerated(true)
            runDir("runs/" + name)
            programArg("--username=Dev")
        }

        named("server") {
            configName = "Fabric Server"

            server()
            ideConfigGenerated(true)
            runDir("runs/" + name)
        }
    }
}

tasks.withType<JavaCompile>().configureEach {
    source(project(":common").sourceSets.getByName("main").allSource)
}

tasks.named<Jar>("sourcesJar").configure {
    from(project(":common").sourceSets.getByName("main").allSource)
}

tasks.withType<Javadoc>().configureEach {
    source(project(":common").sourceSets.getByName("main").allJava)
}

tasks.withType<ProcessResources>().configureEach {
    from(project(":common").sourceSets.getByName("main").resources)
    exclude("**/accesstransformer-nf.cfg")
}

modrinth {
    token = System.getenv("MODRINTH") ?: "Invalid/No API Token Found"
    projectId = "e4dMhzcL"
    versionNumber.set("fabric-${version}")
    versionName = "${version}-fabric-${mcVersion}-${modId}"
    uploadFile.set(tasks.named<RemapJarTask>("remapJar"))
    changelog.set(rootProject.file("changelog.md").readText(Charsets.UTF_8))
    gameVersions.set(listOf(mcVersion, "1.21.2", "1.21.3"))
    versionType = "release"
    loaders.set(listOf("fabric"))
    dependencies {
        required.project("fabric-api")
        optional.project("jei")
    }

    //debugMode = true
    //https://github.com/modrinth/minotaur#available-properties
}

tasks.register<TaskPublishCurseForge>("publishToCurseForge") {
    group = "publishing"
    apiToken = System.getenv("CURSEFORGE") ?: "Invalid/No API Token Found"

    val mainFile = upload(1083303, tasks.remapJar)
    mainFile.releaseType = "release"
    mainFile.addModLoader("Fabric")
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