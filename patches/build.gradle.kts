group = "blazskufca.revanced"

patches {
    about {
        name = "ReVanced Patches by blazskufca"
        description = "Patches for ReVanced"
        source = "git@github.com:blazskufca/revanced-patch.git"
        author = "blazskufca"
        contact = "877198+blazskufca@users.noreply.github.com"
        website = "https://blazskufca.com"
        license = "GNU General Public License v3.0"
    }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    compilerOptions {
        freeCompilerArgs.add("-Xcontext-receivers")
    }
}
