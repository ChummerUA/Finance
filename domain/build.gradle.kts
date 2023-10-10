plugins {
    id(Plugins.Android.library)
    id(Plugins.JetBrains.android)
    id(Plugins.hilt)
    id(Plugins.ksp)
}

android {
    namespace = "com.chummer.domain"
    compileSdk = ConfigData.targetSdkVersion

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    defaultConfig {
        compileSdk = ConfigData.targetSdkVersion
        minSdk = ConfigData.minSdk
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
    api(project(mapOf("path" to ":models")))
    api(project(mapOf("path" to ":network")))
    api(project(mapOf("path" to ":db")))
    implementation(Dependencies.Dagger.hilt)
    ksp(Dependencies.Dagger.daggerCompiler)
    ksp(Dependencies.Dagger.hiltCompiler)
}
