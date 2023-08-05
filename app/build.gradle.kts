plugins {
    id(Plugins.Android.application)
    id(Plugins.JetBrains.android)
}

android {
    namespace = ConfigData.namespace

    defaultConfig {
        applicationId = ConfigData.namespace

        minSdk = ConfigData.minSdk

        compileSdk = ConfigData.targetSdkVersion
        targetSdk = ConfigData.targetSdkVersion

        versionCode = ConfigData.versionCode
        versionName = ConfigData.versionName
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
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
    project(mapOf("path" to ":domain"))
}