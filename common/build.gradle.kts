plugins {
    id("bluelib-convention")
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
    archivesName = "${version}-common-${mcVersion}-${modId}"
}

repositories {
    maven(url = "${rootProject.projectDir}/deps")
    maven("https://maven.blamejared.com/")
}

neoForge {
    neoFormVersion = libs.versions.neoform.get()
    validateAccessTransformers = true
    accessTransformers.files.setFrom("src/main/resources/META-INF/accesstransformer-nf.cfg")

    parchment.minecraftVersion.set(libs.versions.parchment.minecraft.get())
    parchment.mappingsVersion.set(libs.versions.parchment.asProvider().get())
}

dependencies {
    compileOnly(libs.mixin)
    compileOnly(libs.mixinextras.common)
    compileOnlyApi(libs.jei.api)
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


