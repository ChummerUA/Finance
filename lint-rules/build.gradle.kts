plugins {
    id(Plugins.javaLibrary)
    id(Plugins.kotlin)
    id(Plugins.lint)
}

java {
    sourceCompatibility = ConfigData.javaVersion
    targetCompatibility = ConfigData.javaVersion
}

dependencies {
    compileOnly(Dependencies.Lint.api)
    compileOnly(Dependencies.Lint.checks)
}
