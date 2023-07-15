plugins {
    id(Plugins.Android.library)
    id(Plugins.JetBrains.android)
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
    implementation(Dependencies.Infrastructure.network)
}