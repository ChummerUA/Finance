plugins {
    id(Plugins.Android.application)
    id(Plugins.JetBrains.android)
}

android {
    namespace = "${ConfigData.namespaceRoot}.finance"

    defaultConfig {
        applicationId = "${ConfigData.namespaceRoot}.finance"

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
//    implementation(Dependencies)
//    implementation 'androidx.appcompat:appcompat:1.6.1'
//    implementation 'com.google.android.material:material:1.9.0'
}