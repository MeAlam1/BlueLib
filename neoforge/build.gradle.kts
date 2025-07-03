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
val parchmentMcVersion = libs.versions.parchment.minecraft.get()
val parchmentVersion = libs.versions.parchment.asProvider().get()
val neoforgeVersion = libs.versions.neoforge.asProvider().get()

version = ""

base {
    archivesName = "${libs.versions.bluelib.get()}-neoforge-${mcVersion}-bluelib"
}

neoForge {
    version = neoforgeVersion

    accessTransformers.files.setFrom(project(":common").file("src/main/resources/META-INF/accesstransformer-nf.cfg"))
    parchment.minecraftVersion.set(parchmentMcVersion)
    parchment.mappingsVersion.set(parchmentVersion)

    runs {
        configureEach {
            logLevel = org.slf4j.event.Level.DEBUG
        }

        mods.create(modId).sourceSet(project.sourceSets.getByName("main"))

        create("client") {
            client()
        }

        create("server") {
            server()
            programArgument("--nogui")
        }
    }
}

repositories {
    maven(url = "${rootProject.projectDir}/deps")
    maven("https://maven.blamejared.com/")
}

dependencies {
    compileOnly(project(":common"))
    runtimeOnly(libs.jei.neoforge)
}

tasks.withType<Test>().configureEach {
    enabled = false;
}

tasks.named<JavaCompile>("compileJava").configure {
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
}

modrinth {
    token = System.getenv("modrinthKey") ?: "Invalid/No API Token Found"
    projectId = "8BmcQJ2H"
    versionNumber.set(project.version.toString())
    versionName = "NeoForge ${mcVersion}"
    uploadFile.set(tasks.named<Jar>("jar"))
    changelog = rootProject.file("changelog.txt").readText(Charsets.UTF_8)
    gameVersions.set(listOf(mcVersion))
    loaders.set(listOf("neoforge"))

    //debugMode = true
    //https://github.com/modrinth/minotaur#available-properties
}

tasks.register<TaskPublishCurseForge>("publishToCurseForge") {
    group = "publishing"
    apiToken = System.getenv("curseforge.apitoken") ?: "Invalid/No API Token Found"

    val mainFile = upload(388172, tasks.jar)
    mainFile.releaseType = "release"
    mainFile.addModLoader("NeoForge")
    mainFile.addGameVersion(mcVersion)
    mainFile.addJavaVersion("Java 21")
    mainFile.changelog = rootProject.file("changelog.txt").readText(Charsets.UTF_8)

    //debugMode = true
    //https://github.com/Darkhax/CurseForgeGradle#available-properties
}

publishing {
    publishing {
        publications {
            create<MavenPublication>("bluelib") {
                from(components["java"])
                artifactId = base.archivesName.get()
            }
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