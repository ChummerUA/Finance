plugins {
    id(Plugins.Android.library)
    id(Plugins.JetBrains.android)
    id(Plugins.sqlDelight)
    id(Plugins.hilt)
    id(Plugins.ksp)
}

android {
    namespace = "${ConfigData.namespace}.db"

    defaultConfig {
        compileSdk = ConfigData.targetSdkVersion
        minSdk = ConfigData.minSdk
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
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
}

dependencies {
    api(Dependencies.Infrastructure.db)
    implementation(Dependencies.SqlDelight.adapters)
    implementation(Dependencies.Dagger.hilt)
    ksp(Dependencies.Dagger.compiler)
    ksp(Dependencies.Dagger.hiltCompiler)

    coreLibraryDesugaring(Dependencies.desugaring)
}

sqldelight {
    databases {
        create(ConfigData.dbName) {
            packageName.set(ConfigData.namespace)
        }
    }
}
