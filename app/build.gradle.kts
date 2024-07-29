plugins {
    id("com.android.application")
}

android {
    namespace = "com.tanriverdi.adstormeterna"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.tanriverdi.adstormeterna"
        minSdk = 24
        targetSdk = 33
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {


    // Gerçek zamanlı iletişim için Socket.IO istemci kütüphanesi
    implementation("io.socket:socket.io-client:2.1.0")

// Kotlin Multiplatform projeleri için Moko Socket.IO kütüphanesi
    implementation("dev.icerock.moko:socket-io:0.3.0")

// Android UI testleri için UI Automator kütüphanesi
    implementation("androidx.test.uiautomator:uiautomator-v18:2.2.0-alpha1")

// Android test yürütme izleme kütüphanesi
    implementation("androidx.test:monitor:1.7.1")

// Android Test için UI Automator kütüphanesi
    androidTestImplementation("androidx.test.uiautomator:uiautomator:2.3.0")

    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
}