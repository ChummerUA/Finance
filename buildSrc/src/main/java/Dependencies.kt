object Dependencies {
    const val gradle = "com.android.tools.build:gradle:${Versions.gradle}"
    const val kotlinGradle = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}"

    object AndroidX {
        const val root = "androidx"
        const val core = "$root.core:core-ktx:${Versions.ktx}"
        const val appCompat = "$root.appcompat:appcompat:${Versions.appCompat}"
        const val navigationCompose = "$root.navigation:navigation-compose:${Versions.composeNavigation}"
        const val work = "androidx.work:work-runtime-ktx:${Versions.work}"

        object Activity {
            const val root = "${AndroidX.root}.activity"
            const val ktx = "$root:activity-ktx:${Versions.ktx}"
            const val compose = "$root:activity-compose:${Versions.activity}"
        }

        object Lifecycle {
            const val root = "${AndroidX.root}.lifecycle"
            const val process = "$root:lifecycle-process:${Versions.lifecycle}"
            const val runtime = "$root:lifecycle-runtime-ktx:${Versions.lifecycle}"
            const val viewModelCompose = "$root:lifecycle-viewmodel-compose:${Versions.lifecycle}"
        }

        object Hilt {
            const val root = "${AndroidX.root}.hilt"
            const val compiler = "$root:hilt-compiler:${Versions.Hilt.version}"
            const val work = "$root:hilt-work:${Versions.Hilt.version}"
            const val common = "$root:hilt-common:${Versions.Hilt.version}"
            const val compose = "$root:hilt-navigation-compose:${Versions.Hilt.version}"
            const val viewModel = "$root:hilt-lifecycle-viewmodel:${Versions.Hilt.version}"
        }
    }

    object Ktor {
        private const val root = "io.ktor"
        const val core = "$root:ktor-client-core:${Versions.ktor}"
        const val okhttp = "$root:ktor-client-okhttp:${Versions.ktor}"
        const val contentNegotiation = "$root:ktor-client-content-negotiation:${Versions.ktor}"
        const val jsonSerialization = "$root:ktor-serialization-kotlinx-json:${Versions.ktor}"
    }

    object SqlDelight {
        private const val root = "app.cash.sqldelight"

        const val androidDriver = "$root:android-driver:${Versions.sqlDelight}"
        const val gradle = "$root:gradle-plugin:${Versions.sqlDelight}"
        const val adapters = "$root:primitive-adapters:${Versions.sqlDelight}"
    }

    object KotlinX {
        private const val root = "org.jetbrains.kotlinx"

        const val coroutines = "$root:kotlinx-coroutines-android:${Versions.KotlinX.coroutines_version}"
        const val serialization = "$root:kotlinx-serialization-json:${Versions.KotlinX.jsonSerialization}"
        const val immutableCollections = "$root:kotlinx-collections-immutable:${Versions.KotlinX.immutableCollections}"
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

        const val animation = "$root.animation:animation:${Versions.Compose.version}"
        const val foundation = "$root.foundation:foundation:${Versions.Compose.version}"
        const val runtime = "$root.runtime:runtime:${Versions.Compose.version}"
        const val ui = "$root.ui:ui:${Versions.Compose.version}"
        const val compiler = "$root.compiler:compiler:${Versions.Compose.compiler}"
        const val material = "$root.material3:material3:${Versions.Compose.material}"
        const val preview = "$root.ui:ui-tooling:${Versions.Compose.version}"
    }

    object Dagger {
        const val root = "com.google.dagger"
        const val hilt = "$root:hilt-android:${Versions.Hilt.dagger}"
        const val hiltClassPath = "$root:hilt-android-gradle-plugin:${Versions.Hilt.dagger}"
        const val compiler = "$root:dagger-compiler:${Versions.Hilt.dagger}"
        const val hiltCompiler = "$root:hilt-compiler:${Versions.Hilt.dagger}"
    }

    object AssistedInject {
        const val root = "com.squareup.inject"
        const val injections = "$root:assisted-inject-annotations-dagger2:${Versions.Hilt.daggerAssistedInjections}"
        const val processor = "$root:assisted-inject-processor-dagger2:${Versions.Hilt.daggerAssistedInjections}"
    }

    const val desugaring = "com.android.tools:desugar_jdk_libs:${Versions.desugaring}"

    object Firebase {
        const val root = "com.google.firebase"

        const val googleServices = "com.google.gms.gogogle-services"
        const val bom = "$root:firebase-bom:${Versions.firebaseBom}"
        const val analytics = "$root:firebase-analytics"
    }
}
