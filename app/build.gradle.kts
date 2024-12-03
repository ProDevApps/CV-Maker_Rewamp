plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.professorapps.cvmaker"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.professorapps.cvmaker"
        minSdk = 26
        targetSdk = 34
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
    kotlinOptions {
        jvmTarget = "1.8"
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


    implementation ("com.github.bumptech.glide:glide:4.12.0")

    implementation ("com.github.javiersantos:BottomDialogs:1.2.1")
    implementation ("com.github.siyamed:android-shape-imageview:0.9.3")
    implementation ("com.google.code.gson:gson:2.8.9")
    implementation ("com.intuit.sdp:sdp-android:1.0.6")

    implementation ("com.karumi:dexter:4.2.0")

    implementation ("com.github.barteksc:pdfium-android:1.7.1")


    implementation ("com.squareup.picasso:picasso:2.71828")

    implementation ("de.hdodenhof:circleimageview:3.1.0")
    implementation ("io.reactivex.rxjava2:rxandroid:2.1.1")
    implementation ("com.github.permissions-dispatcher:permissionsdispatcher:4.8.0")


    implementation ("com.github.tbruyelle:rxpermissions:0.12")

    implementation ("com.github.shopgun:zoomlayout:3.0.0")

    implementation("com.otaliastudios:zoomlayout:1.9.0")
}

    /*

        implementation 'com.github.barteksc:android-pdf-viewer:2.8.2'
    */



    /*    implementation 'com.shopgun.android:zoomlayout:2.0.0'*/

    /* implementation 'com.isseiaoki:simplecropview:1.1.8'
      implementation 'com.shopgun.android:utils:1.0.2' */
