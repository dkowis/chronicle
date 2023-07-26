
// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {

    extra["kotlinVersion"] = "1.9.0"
    extra["coroutinesVersion"] = "1.6.4"
    extra["archLifecycleVersion"] = "1.1.1"
    extra["gradleVersion"] = "8.1.0"
    extra["supportlibVersion"] = "1.6.1"
    extra["materialLibVersion"] = "1.9.0-beta01" // todo: permanent visible slider label is available from 1.6 (currently in beta) → https://github.com/material-components/material-components-android/releases
    extra["retrofitVersion"] = "2.9.0"
    extra["moshiKotlinVersion"] = "1.14.0"
    extra["okhttpVersion"] = "4.10.0"
    extra["roomVersion"] = "2.5.1"
    extra["frescoVersion"] = "2.4.0"
    //2.15.1 might be the highest, and yet it"s still borked.
    //2.11.4 was original version, it"s heckin old.
    extra["exoplayerVersion"] = "2.18.5"
    extra["exoplayerExtensions"] = "master-SNAPSHOT" //YOLO
    extra["workVersion"] = "2.8.1"
    extra["junitVersion"] = "4.13.2"
    extra["androidxTestRunnerVersion"] = "1.5.0" //TODO: this needs to be split up, because there's newer versions of the rules
    extra["espressoVersion"] = "3.5.1"
    extra["androidxAnnotationVersion"] = "1.6.0"
    extra["lifecycleVersion"] = "2.6.1"
    extra["ossLicenseActivityVersion"] = "17.0.0"
    extra["androidXTestExtKotlinRunnerVersion"] = "1.1.5"
    extra["androidXTestCoreVersion"] = "1.5.0"
    extra["robolectricVersion"] = "4.4"
    extra["archTestingVersion"] = "2.2.0"
    extra["hamcrestVersion"] = "1.3"
    extra["mockKVersion"] = "1.10.0"
    extra["daggerVersion"] = "2.45"
    extra["localBroadcastManagerVersion"] = "1.1.0"
    extra["androidXCustomTabsVersion"] = "1.5.0"
    extra["swipeRefreshLayoutVersion"] = "1.1.0"
    extra["billingVersion"] = "4.0.0"
    extra["iapWrapperVersion"] = "1.2.2"
    extra["constraintLayoutVersion"] = "2.1.4"
    extra["kotlinResultVersion"] = "1.1.11"
    extra["timberVersion"] = "4.7.1"
    extra["seismicVersion"] = "1.0.3"
    extra["appCompatFragmentVersion"] = "1.5.6"


}

plugins {
    /**
     * Use `apply false` in the top-level build.gradle file to add a Gradle
     * plugin as a build dependency but not apply it to the current (root)
     * project. Don't use `apply false` in sub-projects. For more information,
     * see Applying external plugins with same version to subprojects.
     */

    id("com.android.application") version "8.1.0" apply false
    id("com.android.library") version "8.1.0" apply false
    id("org.jetbrains.kotlin.android") version "1.8.22" apply false
    id("com.geekorum.gms.oss-licenses-plugin") version "0.10.6" apply false
    id("org.jlleitschuh.gradle.ktlint") version "11.5.0" apply false
}

tasks.register<Delete>("clean") {
    delete(rootProject.buildDir)
}


// Allow experimental kotlin features, like coroutines
tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    compilerOptions {
        freeCompilerArgs.add("-Xuse-experimental=io.ktor.locations.KtorExperimentalLocationsAPI")
    }
}

val installGitHook = tasks.register<Copy>("installGitHook") {
    from(file("${rootProject.rootDir}/pre-commit"))
    into { file("${rootProject.rootDir}/.git/hooks") }
    fileMode = 777
}

tasks.getByPath(":app:preBuild").dependsOn(installGitHook)


