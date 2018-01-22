import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.tasks.run.BootRun


buildscript {

    extra["kotlinVersion"] = "1.1.60"
    extra["springBootVersion"] = "2.0.0.M7"
    extra["jUnitVersion"] = "5.0.0"
    extra["boostrapVersion"] = "3.3.6"
    extra["jQueryVersion"] = "2.2.4"
    extra["jQueryUIVersion"] = "1.11.4"
    extra["elVersion"] = "3.0.1-b08"

    val springBootVersion: String by extra

    repositories {
        mavenCentral()
        maven("https://repo.spring.io/milestone")
    }

    dependencies {
        classpath("org.springframework.boot:spring-boot-gradle-plugin:$springBootVersion")
        classpath("org.junit.platform:junit-platform-gradle-plugin:1.0.2")
    }
}


plugins {
    val kotlinVersion = "1.1.60"

    id("org.jetbrains.kotlin.jvm") version kotlinVersion
    id("org.jetbrains.kotlin.plugin.spring") version kotlinVersion
    id("io.spring.dependency-management") version "1.0.3.RELEASE"
}

apply {
    plugin("org.springframework.boot")
}


val kotlinVersion: String by extra
val springBootVersion: String by extra
val jUnitVersion: String by extra
val boostrapVersion: String by extra
val jQueryVersion: String by extra
val jQueryUIVersion: String by extra
val elVersion: String by extra

version = springBootVersion

tasks {
    withType<KotlinCompile> {
        kotlinOptions {
            jvmTarget = "1.8"
            freeCompilerArgs = listOf("-Xjsr305=strict")
        }
    }
    withType<BootRun>{
        systemProperty("spring.profiles.active","mysql")
    }
}

repositories {
    mavenCentral()
    maven("https://repo.spring.io/milestone")
}

dependencies {
    compile("org.springframework.boot:spring-boot-starter-actuator")
    compile("org.springframework.boot:spring-boot-starter-cache")
    compile("org.springframework.boot:spring-boot-starter-data-jpa")
    compile("org.springframework.boot:spring-boot-starter-webflux")
    compile("org.springframework.boot:spring-boot-starter-thymeleaf")
    compile("javax.cache:cache-api")
    compile("org.jetbrains.kotlin:kotlin-stdlib:$kotlinVersion")
    compile("org.jetbrains.kotlin:kotlin-reflect:$kotlinVersion")
    compile("org.webjars:webjars-locator")
    compile("org.webjars:jquery:$jQueryVersion")
    compile("org.webjars:jquery-ui:$jQueryUIVersion")
    compile("org.webjars:bootstrap:$boostrapVersion")
    testCompile ( "org.testcontainers:mysql:1.5.1")
    testCompile("org.springframework.boot:spring-boot-starter-test")
    testCompile("org.glassfish:javax.el:$elVersion")
    testCompile("org.junit.jupiter:junit-jupiter-api:$jUnitVersion")
    testRuntime("org.junit.jupiter:junit-jupiter-engine:$jUnitVersion")

    runtime("org.hsqldb:hsqldb")
    runtime("mysql:mysql-connector-java")
    runtime("com.fasterxml.jackson.module:jackson-module-kotlin")
}

