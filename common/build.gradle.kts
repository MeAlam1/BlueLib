plugins {
    id("bluelib-convention")
    alias(libs.plugins.curseforgegradle)
    alias(libs.plugins.moddevgradle)
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