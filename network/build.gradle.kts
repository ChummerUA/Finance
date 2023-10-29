plugins {
    id(Plugins.Android.library)
    id(Plugins.JetBrains.android)
    id(Plugins.JetBrains.serialisation)
    id(Plugins.hilt)
    id(Plugins.ksp)
}

android {
    namespace = "${ConfigData.namespace}.network"

    buildTypes {
        defaultConfig {
            compileSdk = ConfigData.targetSdkVersion
            minSdk = ConfigData.minSdk
        }

        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = ConfigData.javaVersion
        targetCompatibility = ConfigData.javaVersion
    }
    kotlinOptions {
        jvmTarget = ConfigData.jvmTarget
    }
}

dependencies {
    api(Dependencies.Infrastructure.usecase)
    api(Dependencies.Infrastructure.network)

    api(project(mapOf("path" to ":networkModels")))
    api(project(mapOf("path" to ":models")))

    implementation(Dependencies.Ktor.contentNegotiation)
    implementation(Dependencies.Ktor.jsonSerialization)

    implementation(Dependencies.Dagger.hilt)
    ksp(Dependencies.Dagger.compiler)
    ksp(Dependencies.Dagger.hiltCompiler)
}
