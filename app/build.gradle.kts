plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)

    // Añade Hilt y Kapt
    id("com.google.dagger.hilt.android")
    kotlin("kapt")
}

android {
    namespace = "com.ceac.mvvmapp"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.ceac.mvvmapp"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
}

/**
 * Paso 1: Dependencias del módulo app para conexión a backend y presentación.
 *
 * Explicación:
 * - Retrofit + OkHttp: cliente HTTP y adaptador REST.
 * - Moshi + codegen (kapt): serialización JSON eficiente y segura en tiempo de compilación.
 * - Coroutines: concurrencia estructurada para llamadas asíncronas.
 * - DataStore: persistencia ligera (tokens, preferencias).
 * - Hilt: inyección de dependencias para wiring de repos, casos de uso y servicios de red.
 * - Compose + Navigation: UI declarativa y navegación entre pantallas.
 * - Coil: carga de imágenes eficiente en Compose.
 *
 * Paso siguiente: activar el plugin KAPT y configurar el AppModule de Hilt con Retrofit/Moshi/OkHttp.
 */
dependencies {
    // Compose base
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)

    // Networking (HTTP + REST)
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-moshi:2.11.0")

    // JSON (Moshi + Codegen)
    implementation("com.squareup.moshi:moshi:1.15.1")
    kapt("com.squareup.moshi:moshi-kotlin-codegen:1.15.1")

    // Coroutines (núcleo + integración Android)
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.1")

    // DataStore Preferences (persistencia de claves/valores)
    implementation("androidx.datastore:datastore-preferences:1.1.1")

    // Hilt (DI) + navegación con Compose
    implementation("com.google.dagger:hilt-android:2.51.1")
    kapt("com.google.dagger:hilt-android-compiler:2.51.1")
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")

    // Jetpack Compose (BOM + componentes principales + navegación)
    implementation(platform("androidx.compose:compose-bom:2024.05.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.navigation:navigation-compose:2.8.0")

    // Lifecycle para colecciones seguras en Compose
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.8.4")

    // Imágenes en Compose
    implementation("io.coil-kt:coil-compose:2.6.0")

    // Tests
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}