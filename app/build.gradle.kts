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
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8 // Java 8 özelliklerini kullanır
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    packagingOptions {
        resources {
            // Çakışmalardan kaçınmak için bazı meta-inf dosyalarını hariç tutma
            excludes.add("META-INF/INDEX.LIST")
            excludes.add("META-INF/DEPENDENCIES")
            excludes.add("META-INF/NOTICE")
            excludes.add("META-INF/LICENSE")
            excludes.add("META-INF/io.netty.versions.properties")
            // Seçilen dosyaların ilk olarak alınmasını sağlar
            pickFirsts.add("META-INF/INDEX.LIST")
            pickFirsts.add("META-INF/io.netty.versions.properties")
        }
    }
}

dependencies {
    // Socket.IO istemci kütüphanesi
    implementation("io.socket:socket.io-client:2.1.0")

    // MOKO Socket.IO kütüphanesi
    implementation("dev.icerock.moko:socket-io:0.3.0")

    // AndroidX Test Monitor kütüphanesi
    implementation("androidx.test:monitor:1.7.1")
    androidTestImplementation("androidx.test:monitor:1.7.1")

    // AndroidX AppCompat kütüphanesi
    implementation("androidx.appcompat:appcompat:1.7.0")

    // Google Material Design kütüphanesi
    implementation("com.google.android.material:material:1.12.0")

    // ConstraintLayout kütüphanesi
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    // JUnit test kütüphanesi
    testImplementation("junit:junit:4.13.2")

    // AndroidX JUnit ve Espresso kütüphaneleri
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")

    // Google Tink şifreleme kütüphanesi
    implementation("com.google.crypto.tink:tink-android:1.6.1")

    // JSoup HTML parsing kütüphanesi
    implementation("org.jsoup:jsoup:1.13.1")

    // AndroidX test kütüphaneleri
    androidTestImplementation("androidx.test:runner:1.6.1")
    androidTestImplementation("androidx.test:rules:1.6.1")

    // JSON işleme kütüphanesi
    implementation("org.json:json:20210307")

    // Commons IO kütüphanesi
    implementation("commons-io:commons-io:2.12.0")

    // AndroidX Test Rules kütüphanesi
    androidTestImplementation ("androidx.test:rules:1.6.1")

    // AndroidX Test Runner
    androidTestImplementation ("androidx.test:runner:1.6.1")
}
