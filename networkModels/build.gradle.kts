plugins {
    id(Plugins.javaLibrary)
    id(Plugins.JetBrains.jvm)
    id(Plugins.JetBrains.serialisation)
}

java {
    sourceCompatibility = ConfigData.javaVersion
    targetCompatibility = ConfigData.javaVersion
}

dependencies {
    implementation(Dependencies.KotilnX.serialization)

    api(project(mapOf("path" to ":models")))
}
