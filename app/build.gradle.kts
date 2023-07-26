plugins {
    id("org.jlleitschuh.gradle.ktlint")
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("kotlin-parcelize")
    id("com.google.android.gms.oss-licenses-plugin")
}


ktlint {
    debug.set(true)
    disabledRules.set(listOf("no-wildcard-imports"))
    filter {
        exclude {
            //Ignore all the build files.
            it.file.path.contains("build.gradle.kts")
        }
    }
}

kotlin {
    jvmToolchain(17)
}

android {
    compileSdk = 33
    defaultConfig {
        applicationId = "io.github.mattpvaughn.chronicle"
        minSdk =  31
        targetSdk =  33
        versionCode = 26
        versionName = "0.54.0"
        testInstrumentationRunner = "io.github.mattpvaughn.chronicle.application.ChronicleTestRunner"
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled =  true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        getByName("debug") {
            // Note: toggle this away to test IAP
            applicationIdSuffix = ".debug"
            isDebuggable = true
        }
    }
    buildFeatures {
        dataBinding = true
    }
    configurations.all {
        exclude(module = "xpp3")
    }

    // Shared code b/w test and androidTest: mocks "n" stuff
    val SHARED_TEST_DIR = "src/testShared/java"
    sourceSets["main"].java {
        srcDirs(SHARED_TEST_DIR)
    }
    sourceSets["androidTest"].java {
        srcDirs(SHARED_TEST_DIR)
    }
    kapt {
        arguments {
            arg("room.schemaLocation", "$projectDir/schemas".toString())
            arg("room.incremental", "true")
            arg("room.expandProjection", "true")
        }
    }
    flavorDimensions += "freeAsInBeer"
    productFlavors {
        create("freeAsInBeer") {
            dimension = "freeAsInBeer"
            buildConfigField("boolean", "FREE_AS_IN_BEER", "true")
        }
        create("googlePlay") {
            dimension = "freeAsInBeer"
            buildConfigField("boolean", "FREE_AS_IN_BEER", "false")
        }
    }
    lint {
        abortOnError = false
    }
    namespace = "io.github.mattpvaughn.chronicle"
}


dependencies {
    // Kotlin
    implementation("org.jetbrains.kotlin:kotlin-stdlib:${rootProject.extra["kotlinVersion"]}")

    // Material components
    implementation("com.google.android.material:material:${rootProject.extra["materialLibVersion"]}")
    implementation("androidx.appcompat:appcompat:${rootProject.extra["supportlibVersion"]}")
    implementation("androidx.fragment:fragment-ktx:${rootProject.extra["appCompatFragmentVersion"]}")
    implementation("androidx.recyclerview:recyclerview:1.3.0")
    implementation("androidx.constraintlayout:constraintlayout:${rootProject.extra["constraintLayoutVersion"]}")

    // AndroidX
    implementation("androidx.annotation:annotation:${rootProject.extra["androidxAnnotationVersion"]}")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:${rootProject.extra["lifecycleVersion"]}")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:${rootProject.extra["lifecycleVersion"]}")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:${rootProject.extra["lifecycleVersion"]}")
    implementation("androidx.lifecycle:lifecycle-service:${rootProject.extra["lifecycleVersion"]}")
    implementation("androidx.lifecycle:lifecycle-process:${rootProject.extra["lifecycleVersion"]}")

    // Google Play Billing
    implementation("com.github.akshaaatt:Google-IAP:${rootProject.extra["iapWrapperVersion"]}")

    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:${rootProject.extra["retrofitVersion"]}")
    implementation("com.squareup.retrofit2:converter-moshi:${rootProject.extra["retrofitVersion"]}")
    implementation("com.squareup.moshi:moshi-kotlin:${rootProject.extra["moshiKotlinVersion"]}")
    kapt("com.squareup.moshi:moshi-kotlin-codegen:${rootProject.extra["moshiKotlinVersion"]}")

    // Kotlin coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${rootProject.extra["coroutinesVersion"]}")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:${rootProject.extra["coroutinesVersion"]}")

    // OkHttp
    implementation("com.squareup.okhttp3:okhttp:${rootProject.extra["okhttpVersion"]}")
    implementation("com.squareup.okhttp3:logging-interceptor:${rootProject.extra["okhttpVersion"]}")

    // Timber Logging
    implementation("com.jakewharton.timber:timber:${rootProject.extra["timberVersion"]}")

    // Fetch download manager
    implementation("androidx.tonyodev.fetch2:xfetch2:3.1.6")

    // Room DB manager
    implementation("androidx.room:room-runtime:${rootProject.extra["roomVersion"]}")
    implementation("androidx.room:room-ktx:${rootProject.extra["roomVersion"]}")
    kapt("androidx.room:room-compiler:${rootProject.extra["roomVersion"]}")

    // Dagger dependency injection
    implementation("com.google.dagger:dagger:${rootProject.extra["daggerVersion"]}")
    kapt("com.google.dagger:dagger-compiler:${rootProject.extra["daggerVersion"]}")

    // For Plex OAuth implementation
    implementation("androidx.browser:browser:${rootProject.extra["androidXCustomTabsVersion"]}")

    // AssistedInject to provide runtime args to Dagger injected objects
    // TODO- need further research into how this works so we can add AssistInject without adding
    //       a lot more boilerplate
//    compileOnly "com.squareup.inject:assisted-inject-annotations-dagger2:0.5.2"
//    kapt "com.squareup.inject:assisted-inject-processor-dagger2:0.5.2"

    // TODO: Remove glide, only included to delete the cache v0.45
    implementation ("com.github.bumptech.glide:glide:4.11.0") {
        // exclude okhttp import in favor of our own import
        exclude(group = "com.squareup.okhttp:")
    }


        // Fresco - image loading
    implementation("com.facebook.fresco:fresco:2.6.0")
    implementation("com.facebook.fresco:imagepipeline-okhttp3:2.4.0")

    // LocalBroadcastManager
    implementation("androidx.localbroadcastmanager:localbroadcastmanager:${rootProject.extra["localBroadcastManagerVersion"]}")

    // Exoplayer
    implementation("com.google.android.exoplayer:exoplayer-core:${rootProject.extra["exoplayerVersion"]}")
    implementation("com.google.android.exoplayer:exoplayer-ui:${rootProject.extra["exoplayerVersion"]}")
    implementation("com.google.android.exoplayer:extension-mediasession:${rootProject.extra["exoplayerVersion"]}")

    // ExoPlayer extensions for FLAC and OPUS file types
    implementation("com.github.PaulWoitaschek.ExoPlayer-Extensions:extension-opus:${rootProject.extra["exoplayerExtensions"]}") {
        isTransitive = false
    }
    implementation("com.github.PaulWoitaschek.ExoPlayer-Extensions:extension-flac:${rootProject.extra["exoplayerExtensions"]}"){
        isTransitive = false
    }

    // WorkManager helps coordinate background syncing of local data w.r.t. network conditions
    implementation("androidx.work:work-runtime-ktx:${rootProject.extra["workVersion"]}")

    // Android team"s license displaying library- creates an activity showing all dependencies
    // by pulling from maven
    implementation("com.google.android.gms:play-services-oss-licenses:${rootProject.extra["ossLicenseActivityVersion"]}")

    // Swipe-to-refresh
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:${rootProject.extra["swipeRefreshLayoutVersion"]}")

    // Seismic- for shake detection
    implementation("com.squareup:seismic:${rootProject.extra["seismicVersion"]}")

    // Kotlin-result: TODO remove because Result is now accessible in our current build
    implementation("com.michael-bull.kotlin-result:kotlin-result:${rootProject.extra["kotlinResultVersion"]}")

    // Test for memory leaks
    debugImplementation("com.squareup.leakcanary:leakcanary-android:2.8.1")


    /* ~~~~~~~~~~~~ Local Tests ~~~~~~~~~~ */
    testImplementation("junit:junit:${rootProject.extra["junitVersion"]}")
    testImplementation("androidx.test.ext:junit-ktx:${rootProject.extra["androidXTestExtKotlinRunnerVersion"]}")
    testImplementation("androidx.test:core-ktx:${rootProject.extra["androidXTestCoreVersion"]}")
    testImplementation("org.robolectric:robolectric:${rootProject.extra["robolectricVersion"]}")
    testImplementation("androidx.room:room-testing:${rootProject.extra["roomVersion"]}")
    testImplementation("androidx.arch.core:core-testing:${rootProject.extra["archTestingVersion"]}")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:${rootProject.extra["coroutinesVersion"]}")
    testImplementation("org.hamcrest:hamcrest-all:${rootProject.extra["hamcrestVersion"]}")
    testImplementation("io.mockk:mockk:${rootProject.extra["mockKVersion"]}") {
        exclude(group = "org.jetbrains.kotlin", module = "kotlin-reflect")
    }

    /* ~~~~~~~~~~~~ Instrumented tests ~~~~~~~~~~ */
    androidTestImplementation("junit:junit:${rootProject.extra["junitVersion"]}")
    androidTestImplementation("androidx.test:runner:${rootProject.extra["androidxTestRunnerVersion"]}")
    androidTestImplementation("androidx.test:rules:${rootProject.extra["androidxTestRunnerVersion"]}")
    androidTestImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:${rootProject.extra["coroutinesVersion"]}")
    androidTestImplementation("androidx.test.ext:junit:${rootProject.extra["androidXTestExtKotlinRunnerVersion"]}")
    androidTestImplementation("androidx.test.ext:junit-ktx:${rootProject.extra["androidXTestExtKotlinRunnerVersion"]}")
    androidTestImplementation("androidx.test.espresso:espresso-core:${rootProject.extra["espressoVersion"]}")
    androidTestImplementation("androidx.test.espresso:espresso-contrib:${rootProject.extra["espressoVersion"]}")
    androidTestImplementation("io.mockk:mockk:${rootProject.extra["mockKVersion"]}") {
        exclude(group = "org.jetbrains.kotlin", module=  "kotlin-reflect")
    }

    androidTestImplementation("com.google.dagger:dagger:${rootProject.extra["daggerVersion"]}")
    kaptAndroidTest("com.google.dagger:dagger-compiler:${rootProject.extra["daggerVersion"]}")

}
