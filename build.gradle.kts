plugins {
    id("dev.architectury.loom")
    id("architectury-plugin")
}

val minecraft = stonecutter.current.version

version = "${mod.version}+$minecraft"
base {
    archivesName.set("${mod.id}-common")
}

architectury.common(stonecutter.tree.branches.mapNotNull {
    if (stonecutter.current.project !in it) null
    else it.project.prop("loom.platform")
})

repositories {
    maven("https://maven.architectury.dev")
    maven("https://maven.ftb.dev/releases")
    maven("https://www.cursemaven.com") { content { includeGroup("curse.maven") } }
}

dependencies {
    minecraft("com.mojang:minecraft:$minecraft")
    mappings(loom.officialMojangMappings())
    modImplementation("net.fabricmc:fabric-loader:${mod.dep("fabric_loader")}")

    modCompileOnly("dev.architectury:architectury-fabric:${mod.dep("architectury")}") { isTransitive = false }
    modCompileOnly("dev.ftb.mods:ftb-library-fabric:${mod.dep("ftb_library")}") { isTransitive = false }
    modCompileOnly("dev.ftb.mods:ftb-quests-fabric:${mod.dep("ftb_quests")}") { isTransitive = false }

    compileOnly("curse.maven:quests-additions-580129:6004490")
}

java {
    withSourcesJar()
    val java = if (stonecutter.eval(minecraft, ">=1.20.5"))
        JavaVersion.VERSION_21 else JavaVersion.VERSION_17
    targetCompatibility = java
    sourceCompatibility = java
}

tasks.build {
    group = "versioned"
    description = "Must run through 'chiseledBuild'"
}
