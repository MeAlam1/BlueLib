plugins {
    id("bluelib-convention")
    alias(libs.plugins.curseforgegradle)
    alias(libs.plugins.moddevgradle)
    id("com.diffplug.spotless") version "6.25.0"
    id("com.github.hierynomus.license") version "0.16.1"
}

repositories {
    maven(url = "${rootProject.projectDir}/deps")
}

version = libs.versions.bluelib.get()

base {
    archivesName = "${version}-common-${libs.versions.minecraft.asProvider().get()}-bluelib"
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
    jarJar(libs.molang)
    compileOnly(libs.molang)

    // Only enable for testing as needed
    // Disable before publishing
    //implementation(libs.examplemod.common)
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
        endWithNewline()
        removeUnusedImports()
        toggleOffOn()

        // Pin version to 4.31 due to Spotless bug https://github.com/diffplug/spotless/issues/1992
        eclipse("4.31").configFile(rootProject.file("codeformat/formatter-config.xml"))

        importOrder()
        custom("jetbrainsNullable") { fileContents: String ->
            fileContents.replace("javax.annotation.Nullable", "org.jetbrains.annotations.Nullable")
        }

        bumpThisNumberIfACustomStepChanges(2)
    }
}

license {
    header = rootProject.file("HEADER")
    include("**/*.java")
    strictCheck = true

    mapping("java", "SLASHSTAR_STYLE")

    skipExistingHeaders = false
}

tasks.withType<Javadoc> {
    exclude("**/*.kt")
    (options as StandardJavadocDocletOptions).addStringOption("Xdoclint:none", "-quiet")
    isFailOnError = false
}


