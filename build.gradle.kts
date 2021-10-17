import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "2.5.5"
	id("io.spring.dependency-management") version "1.0.11.RELEASE"
	kotlin("jvm") version "1.5.31"
	kotlin("plugin.spring") version "1.5.31"
	kotlin("plugin.jpa") version "1.5.31"
	kotlin("kapt") version "1.5.31"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	runtimeOnly("com.h2database:h2")
	runtimeOnly("org.mariadb.jdbc:mariadb-java-client")
	annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	implementation("org.springframework.boot:spring-boot-starter-web")

	// mapstruct
	implementation("org.mapstruct:mapstruct:1.4.2.Final")
	kapt("org.mapstruct:mapstruct-processor:1.4.2.Final")
	kaptTest("org.mapstruct:mapstruct-processor:1.4.2.Final")

	// kotest
	testImplementation("io.kotest:kotest-runner-junit5:4.3.0") // for kotest framework
	testImplementation("io.kotest:kotest-assertions-core:4.3.0") // for kotest core jvm assertions
	testImplementation("io.kotest:kotest-property:4.3.0") // for
	testImplementation("io.kotest:kotest-extensions-spring:4.3.0")

	// fixture
	testImplementation("com.appmattus.fixture:fixture:1.1.0")

	//mokcito
	testImplementation("org.mockito:mockito-inline")

	//jackson
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")


}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "11"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
