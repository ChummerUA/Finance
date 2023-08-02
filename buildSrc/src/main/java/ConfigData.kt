import org.gradle.api.JavaVersion

object ConfigData {
    const val targetSdkVersion = 34
    const val minSdk = 24

    val javaVersion = JavaVersion.VERSION_1_8
    const val jvmTarget = "1.8"

    const val versionCode = 1
    const val versionName = "1.0"

    const val namespace = "com.chummer.finance"

    const val dbName = "ChummerFinanceDatabase"
}
