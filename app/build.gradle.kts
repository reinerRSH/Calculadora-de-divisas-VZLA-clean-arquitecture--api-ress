plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.dolarcotizacion"
    compileSdk {
        version = release(36) {
            minorApiLevel = 1
        }
    }

    buildFeatures{
        viewBinding = true
    }

    defaultConfig {
        applicationId = "com.example.dolarcotizacion"
        minSdk = 26
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)


    val lifecycle_version = "2.8.0" // Puedes usar la versión más reciente disponible
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycle_version")

    // LiveData (Si también lo estás usando para observar en la View)
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycle_version")

    // Opcional: Extensiones de actividad y fragmento para usar 'by viewModels()'
    implementation("androidx.activity:activity-ktx:1.9.0")
    implementation("androidx.fragment:fragment-ktx:1.7.0")

    implementation("com.google.code.gson:gson:2.10.1")

    // Convertidor de Retrofit que usa Gson internamente
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
}