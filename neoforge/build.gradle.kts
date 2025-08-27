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
val bluelibVersion = libs.versions.bluelib.get()

if (bluelibVersion.isEmpty()) {
    throw GradleException("libs.versions.bluelib is empty. Please set a valid version in your version catalog.")
}

version = bluelibVersion


base {
    archivesName = "${version}-neoforge-${mcVersion}-${modId}"
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
    token = System.getenv("MODRINTH") ?: "Invalid/No API Token Found"
    projectId = "e4dMhzcL"
    versionNumber.set("neoforge-${version}")
    versionName = "${version}-neoforge-${mcVersion}-${modId}"
    uploadFile.set(tasks.named<Jar>("jar"))
    changelog = rootProject.file("changelog.md").readText(Charsets.UTF_8)
    gameVersions.set(listOf(mcVersion, "1.21.2", "1.21.3"))
    versionType = "release"
    loaders.set(listOf("neoforge"))
    dependencies {
        optional.project("jei")
    }

    //debugMode = true
    //https://github.com/modrinth/minotaur#available-properties
}

tasks.register<TaskPublishCurseForge>("publishToCurseForge") {
    group = "publishing"
    apiToken = System.getenv("CURSEFORGE") ?: "Invalid/No API Token Found"

    val mainFile = upload(1083303, tasks.jar)
    mainFile.displayName = "${version}-neoforge-${mcVersion}-${modId}"
    mainFile.releaseType = "release"
    mainFile.addModLoader("NeoForge")
    mainFile.addGameVersion(mcVersion, "1.21.2", "1.21.3")
    mainFile.addJavaVersion("Java 21")
    mainFile.changelog = rootProject.file("changelog.md").readText(Charsets.UTF_8)
    
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