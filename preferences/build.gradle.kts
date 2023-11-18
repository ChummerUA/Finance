plugins {
    id(Plugins.Android.library)
    id(Plugins.JetBrains.android)
    id(Plugins.hilt)
    id(Plugins.ksp)
}

android {
    namespace = "com.chummer.preferences"
    compileSdk = ConfigData.targetSdkVersion

    buildTypes {
        release {
            isMinifyEnabled = true
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
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = ConfigData.javaVersion
        targetCompatibility = ConfigData.javaVersion
    }
    kotlinOptions {
        jvmTarget = ConfigData.jvmTarget
    }
}

dependencies {
    api(Dependencies.Infrastructure.usecase)
    api(Dependencies.Infrastructure.preferences)
    implementation(Dependencies.Dagger.hilt)
    ksp(Dependencies.Dagger.compiler)
    ksp(Dependencies.Dagger.hiltCompiler)
    coreLibraryDesugaring(Dependencies.desugaring)
}
