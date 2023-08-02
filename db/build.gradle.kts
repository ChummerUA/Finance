plugins {
    id(Plugins.Android.library)
    id(Plugins.JetBrains.android)
    id(Plugins.sqlDelight)
}

android {
    namespace = "${ConfigData.namespace}.db"

    defaultConfig {
        compileSdk = ConfigData.targetSdkVersion
        minSdk = ConfigData.minSdk
    }

    buildTypes {
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
    api(Dependencies.Infrastructure.db)
}

sqldelight {
    databases {
        create(ConfigData.dbName) {
            packageName.set(ConfigData.namespace)
            dialect(Dependencies.SqlDelight.dialect)
        }
    }
}
