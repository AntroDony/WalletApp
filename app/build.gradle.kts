import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kotlin.parcelize)
}

android {
    namespace = "com.ancraz.mywallet"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.ancraz.mywallet"
        minSdk = 26
        targetSdk = 35
        versionCode = 6
        versionName = "1.0.5"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        val localProperties = gradleLocalProperties(rootDir, providers)
        val apiKey: String = localProperties.getProperty("API_KEY") ?: ""


        buildConfigField("String", "API_BASE_URL", "\"https://api.currencyfreaks.com/v2.0/\"")
        buildConfigField("String", "API_KEY", "\"$apiKey\"")
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.15"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    //Android
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.ui.text.google.fonts)
    coreLibraryDesugaring(libs.desugar.jdk.libs)
    implementation(libs.androidx.lifecycle.viewmodel.navigation3)
    implementation(libs.android.material)

    //Compose
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.bundles.compose)
    debugImplementation(libs.bundles.compose.debug)

    //Hilt
    implementation(libs.android.dagger.hilt)
    implementation(libs.hilt.navigation.compose)
    kapt(libs.hilt.compiler)


    //Room
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    kapt(libs.room.compiler)


    //Test and debug
    androidTestImplementation(platform(libs.androidx.compose.bom))
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)


    //Gson
    implementation(libs.gson)

    //Navigation
    implementation(libs.androidx.navigation3.runtime)
    implementation(libs.androidx.navigation3.ui)

    //ktor
    implementation(libs.bundles.ktor)

    //DataStore
    implementation(libs.datastore.preferences)

    //Coil
    implementation(libs.coil.compose)
    implementation(libs.coil.svg)

    //Glance
    implementation(libs.glance.widget)
    implementation(libs.glance.material3)

    //Splash Screen
    implementation(libs.androidx.splash.screen)
}