import org.jetbrains.kotlin.config.KotlinCompilerVersion

plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-android-extensions")
    id("kotlin-kapt")
    id("androidx.navigation.safeargs.kotlin")
    id("com.hiya.jacoco-android")
}

android {
    compileSdkVersion(Config.compileSdk)

    defaultConfig {
        applicationId = "com.adammcneilly.pokedex"
        minSdkVersion(Config.minSdk)
        targetSdkVersion(Config.targetSdk)
        versionCode = Config.appVersionCode
        versionName = Config.appVersionName
        testInstrumentationRunner = "com.adammcneilly.pokedex.MockTestRunner"
    }

    buildFeatures {
        // Enables Jetpack Compose for this module
        compose = true
    }

    buildTypes {
        getByName("debug") {
            buildConfigField(
                "Integer",
                "PORT",
                "8080"
            )
        }
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    dataBinding {
        isEnabled = true
    }

    testOptions {
        animationsDisabled = true
    }

    packagingOptions {
        exclude("META-INF/atomicfu.kotlin_module")
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    composeOptions {
        kotlinCompilerVersion = "1.3.70-dev-withExperimentalGoogleExtensions-20200424"
        kotlinCompilerExtensionVersion = "0.1.0-dev13"
    }
}

repositories {
    maven("http://dl.bintray.com/androidessence/maven")
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation(project(":image-loader"))
    implementation(project(":database"))
    implementation(project(":network"))
    implementation(project(":core"))
    implementation(kotlin("stdlib", KotlinCompilerVersion.VERSION))
    debugImplementation(Dependencies.fragmentTesting) {
        exclude("androidx.test", "core")
    }

    addDependencies(Dependencies.appDependencies)

    implementation("androidx.ui:ui-tooling:0.1.0-dev13")
    implementation("androidx.ui:ui-layout:0.1.0-dev13")
    implementation("androidx.ui:ui-material:0.1.0-dev13")
    implementation("androidx.compose:compose-runtime:0.1.0-dev13")
}

configure<io.gitlab.arturbosch.detekt.extensions.DetektExtension> {
    config = files("${rootProject.projectDir}/config/detekt/detekt.yml")
}

configure<JacocoPluginExtension> {
    toolVersion = "0.8.5"
}

configure<com.hiya.plugins.JacocoAndroidUnitTestReportExtension> {
    csv.enabled(false)
    html.enabled(true)
    xml.enabled(false)

    excludes = this.excludes + listOf(
        "**/databinding/**",
        "**/views/**",
        "**/*DataBinder*.*",
        "**/*DataBinding*.*",
        "**/*Fragment*.*",
        "**/*Activity*.*",
        "**/*BaseObservableViewModel.class",
        "**/*PokeApp.class",
        "**/*BindingAdaptersKt.class"
    )
}