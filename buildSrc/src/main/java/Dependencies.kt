object Dependencies {
    const val gradle = "com.android.tools.build:gradle:${Versions.gradle}"
    const val kotlinGradle = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}"

    object AndroidX {
        const val root = "androidx"
        const val core = "$root.core:core-ktx:${Versions.ktx}"
        const val activity = "$root.activity:activity-ktx:${Versions.ktx}"
        const val appCompat = "$root.appcompat:appcompat:${Versions.appCompat}"
        const val lifecycleProcess = "$root.lifecycle:lifecycle-process:${Versions.lifecycle}"
        const val lifecycleRuntime = "$root.lifecycle:lifecycle-runtime-ktx:${Versions.lifecycle}"
        const val activityCompose = "$root.activity:activity-compose:${Versions.activity}"
        const val navigationCompose = "$root.navigation:navigation-compose:${Versions.composeNavigation}"
        const val viewModelCompose = "$root.lifecycle:lifecycle-viewmodel-compose:${Versions.lifecycle}"
    }

    object Ktor {
        private const val root = "io.ktor"
        const val core = "$root:ktor-client-core:${Versions.ktor}"
        const val okhttp = "$root:ktor-client-okhttp:${Versions.ktor}"
    }

    object SqlDelight {
        private const val root = "app.cash.sqldelight"

        const val androidDriver = "$root:android-driver:${Versions.sqlDelight}"
        const val gradle = "$root:gradle-plugin:${Versions.sqlDelight}"
        const val adapters = "$root:primitive-adapters:${Versions.sqlDelight}"
    }

    object KotilnX {
        private const val root = "org.jetbrains.kotlinx"

        const val coroutines = "$root:kotlinx-coroutines-android:${Versions.KotlinX.coroutines_version}"
        const val serialization = "$root:kotlinx-serialization-json:${Versions.KotlinX.jsonSerialization}"
    }

    const val dataStore = "androidx.datastore:datastore-preferences:${Versions.dataStore}"

    object Infrastructure {
        private const val root = "com.chummer.infrastructure"

        const val usecase = "$root:usecase:${Versions.infrastructure}"
        const val db = "$root:db:${Versions.infrastructure}"
        const val network = "$root:network:${Versions.infrastructure}"
        const val preferences = "$root:preferences:${Versions.infrastructure}"
    }

    object Lint {
        private const val root = "com.android.tools.lint"

        const val api = "$root:lint-api:${Versions.buildTools}"
        const val checks = "$root:lint-checks:${Versions.buildTools}"
    }

    object Compose {
        private const val root = "androidx.compose"

        const val animation = "$root.animation:animation:${Versions.compose}"
        const val foundation = "$root.foundation:foundation:${Versions.compose}"
        const val runtime = "$root.runtime:runtime:${Versions.compose}"
        const val ui = "$root.ui:ui:${Versions.compose}"
        const val compiler = "$root.compiler:compiler:${Versions.composeCompiler}"
//        const val  = "$root.::${Versions.compose}"
    }
}
