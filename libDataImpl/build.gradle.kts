import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("java")
    kotlin("jvm")
}

dependencies {
    implementation(kotlin("stdlib-jdk7"))
    implementation(group = "com.squareup.retrofit2", name = "retrofit", version = "2.6.1")
}


val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
    jvmTarget = "1.6"
}
val compileTestKotlin: KotlinCompile by tasks
compileTestKotlin.kotlinOptions {
    jvmTarget = "1.6"
}