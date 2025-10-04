import com.bmuschko.gradle.docker.tasks.image.DockerBuildImage
import com.bmuschko.gradle.docker.tasks.image.DockerPushImage
import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
	kotlin("jvm") version "1.9.25"
	kotlin("plugin.spring") version "1.9.25"
	id("org.springframework.boot") version "3.5.6"
	id("io.spring.dependency-management") version "1.1.7"
    id("com.bmuschko.docker-remote-api") version "9.4.0"
}

group = "kr.dhkim92"
version = "3.0.3"
description = "dhkim92 Spring Boot Webflux Blog Backend"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

repositories {
	mavenCentral()
}

dependencies {
    implementation("io.netty:netty-resolver-dns-native-macos:4.1.100.Final:osx-aarch_64")
	implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")
	implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-client")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
	implementation("com.github.f4b6a3:uuid-creator:6.1.1")  // UUID Creator 라이브러리
	implementation("org.flywaydb:flyway-core")
	implementation("org.flywaydb:flyway-database-postgresql")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
	implementation("org.springframework:spring-jdbc")
    implementation("com.auth0:java-jwt:4.5.0")

//    implementation("com.h2database:h2")
	// SpringDoc OpenAPI (Swagger) for WebFlux
	implementation("org.springdoc:springdoc-openapi-starter-webflux-ui:2.4.0")
    // Sanitizer
    implementation("com.googlecode.owasp-java-html-sanitizer:owasp-java-html-sanitizer:20240325.1")
    // Thumbnail
    implementation("net.coobird:thumbnailator:0.4.20")

	runtimeOnly("org.postgresql:postgresql")
	runtimeOnly("org.postgresql:r2dbc-postgresql")
//    testImplementation("io.r2dbc:r2dbc-h2")
//    testImplementation("com.h2database:h2")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("io.projectreactor:reactor-test")
	testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
	testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test")
	testImplementation("org.springframework.security:spring-security-test")
    testImplementation("io.kotest:kotest-runner-junit5:4.4.3")
    testImplementation("io.kotest:kotest-assertions-core:4.4.3")
    testImplementation("io.kotest:kotest-extensions-spring:4.4.3")
    testImplementation("com.ninja-squad:springmockk:4.0.2")
    testImplementation("io.mockk:mockk:1.13.5")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<BootJar> {
    archiveVersion.set("latest")
}

tasks.register<DockerBuildImage>("buildDockerImage") {
    dependsOn("bootJar")
    inputDir.set(file(".")) // Dockerfile이 위치한 경로 (프로젝트 root 경로)
    images.add("elensar92/blog-backend:${version}")
    group = "docker"
}

tasks.register<DockerPushImage>("pushDockerImage") {
    dependsOn("buildDockerImage")
    images.add("elensar92/blog-backend:${version}")
    group = "docker"
}

kotlin {
	compilerOptions {
		freeCompilerArgs.addAll("-Xjsr305=strict")
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
