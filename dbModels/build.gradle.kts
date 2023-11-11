import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id(Plugins.javaLibrary)
    id(Plugins.JetBrains.jvm)
    id(Plugins.JetBrains.serialisation)
}

java {
    sourceCompatibility = ConfigData.javaVersion
    targetCompatibility = ConfigData.javaVersion
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = ConfigData.jvmTarget
}

dependencies {
    implementation(Dependencies.KotlinX.serialization)
}
