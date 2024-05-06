plugins {
    id("com.android.application")
}

android {
    namespace = "com.lista.listacompra"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.lista.listacompra"
        minSdk = 33
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {

    implementation("androidx.appcompat:appcompat:1.6.1") // Librerias de android
    implementation("com.google.android.material:material:1.11.0") // Librerias de android
    implementation("androidx.constraintlayout:constraintlayout:2.1.4") // Librerias de android
    implementation("androidx.recyclerview:recyclerview:1.2.1")

    implementation("com.google.android.gms:play-services-maps:18.2.0")
    implementation("androidx.lifecycle:lifecycle-common:2.5.1")

    implementation("com.fasterxml.jackson.core:jackson-core:2.15.3") // Librerias para manejar los JSON devueltos por los API
    implementation("com.fasterxml.jackson.core:jackson-databind:2.15.3") // Librerias para manejar los JSON devueltos por los API
    implementation("com.fasterxml.jackson.core:jackson-annotations:2.15.3") // Librerias para manejar los JSON devueltos por los API

    implementation("com.squareup.picasso:picasso:2.71828") // Libreria para mostrar las imagenes de los productos en la interfaz

    implementation("androidx.room:room-runtime:2.6.1")
    implementation("com.android.car.ui:car-ui-lib:2.6.0")
    implementation("com.google.android.gms:play-services-maps:18.2.0") // Librerias para acceder a SQLite desde la aplicación
    annotationProcessor ("androidx.room:room-compiler:2.6.1") // Librerias para acceder a SQLite desde la aplicación

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    // https://mvnrepository.com/artifact/com.github.javafaker/javafaker
    implementation("com.github.javafaker:javafaker:1.0.2")
}
