
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    // Apply the java-library plugin to add support for Java Library
    `java-library`
    id("org.jetbrains.kotlin.jvm") version "1.4.10"
}

repositories {
    // Use jcenter for resolving dependencies.
    // You can declare any Maven/Ivy/file repository here.
    jcenter()
}

dependencies {

    implementation("com.google.cloud.functions:functions-framework-api:1.0.2")
    implementation("org.openrewrite:rewrite-java-11:5.5.0")
    implementation("org.projectlombok:lombok:1.18.16")
    implementation("com.google.cloud.functions:functions-framework-api:1.0.2")
    implementation("com.google.cloud.functions:functions-framework-api:1.0.2")

    // Use JUnit Jupiter API for testing.
    testImplementation("org.openrewrite:rewrite-test:5.5.0")
    testImplementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.4.0")
    testImplementation("org.jetbrains.kotlin:kotlin-reflect:1.4.0")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.2")
    testImplementation("org.mockito:mockito-core:3.5.15")

    // Use JUnit Jupiter Engine for testing.
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.6.2")
}

tasks.withType(KotlinCompile::class.java) {
    kotlinOptions {
        jvmTarget = "1.8"
    }
    destinationDir.mkdirs()
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions.jvmTarget = "11"

val test by tasks.getting(Test::class) {
    // Use junit platform for unit tests
    useJUnitPlatform()
}
