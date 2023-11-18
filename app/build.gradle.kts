plugins {
    id(Plugins.Android.application)
    id(Plugins.JetBrains.android)
    id(Plugins.hilt)
    id(Plugins.ksp)
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
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = ConfigData.javaVersion
        targetCompatibility = ConfigData.javaVersion
    }
    kotlinOptions {
        jvmTarget = ConfigData.jvmTarget
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = Versions.Compose.compiler
    }
}

dependencies {
    implementation(project(mapOf("path" to ":domain")))
    implementation(project(mapOf("path" to ":preferences")))

    implementation(Dependencies.Compose.compiler)
    implementation(Dependencies.Compose.runtime)
    implementation(Dependencies.Compose.foundation)
    implementation(Dependencies.Compose.ui)
    implementation(Dependencies.Compose.animation)

    implementation(Dependencies.AndroidX.core)
    implementation(Dependencies.AndroidX.appCompat)

    implementation(Dependencies.AndroidX.Lifecycle.process)
    implementation(Dependencies.AndroidX.Lifecycle.runtime)
    implementation(Dependencies.AndroidX.Lifecycle.viewModelCompose)

    implementation(Dependencies.AndroidX.Activity.compose)
    implementation(Dependencies.AndroidX.navigationCompose)

    implementation(Dependencies.AndroidX.Hilt.compose)
    implementation(Dependencies.AndroidX.Hilt.common)

    implementation(Dependencies.Dagger.hilt)
    ksp(Dependencies.Dagger.hiltCompiler)
    implementation(Dependencies.AndroidX.Hilt.work)
    ksp(Dependencies.AndroidX.Hilt.compiler)

    implementation(Dependencies.KotlinX.immutableCollections)

    implementation(Dependencies.AndroidX.work)
    coreLibraryDesugaring(Dependencies.desugaring)
}