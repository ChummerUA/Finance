object Plugins {
    const val javaLibrary = "java-library"
    const val kotlin = "kotlin"

    object Android {
        const val application = "com.android.application"
        const val library = "com.android.library"
    }

    object JetBrains {
        const val android = "org.jetbrains.kotlin.android"
        const val jvm = "org.jetbrains.kotlin.jvm"
        const val serialization = "org.jetbrains.kotlin.plugin.serialization"
    }

    const val serialization = "plugin.serialization"

    const val sqlDelight = "app.cash.sqldelight"

    const val lint = "com.android.lint"

    const val hilt = "com.google.dagger.hilt.android"

    const val ksp = "com.google.devtools.ksp"
}
