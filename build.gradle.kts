plugins {
	java
	id("org.springframework.boot") version "3.4.1"
	id("io.spring.dependency-management") version "1.1.7"
}

group = "ge.giorgi.example"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

repositories {
	mavenCentral()
}

dependencies {
	compileOnly("org.projectlombok:lombok:1.18.36")
	annotationProcessor("org.projectlombok:lombok:1.18.36")

	implementation("org.springframework.boot:spring-boot-starter")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("com.nimbusds:nimbus-jose-jwt:10.0.1")
	implementation("org.postgresql:postgresql:42.7.4")
	implementation("org.liquibase:liquibase-core:4.30.0")
//	implementation("com.cloudinary:cloudinary:1.32.0")
	implementation("com.cloudinary:cloudinary-http44:1.36.0")

	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.mockito:mockito-core:5.8.0")
	testImplementation("org.mockito:mockito-junit-jupiter:5.8.0")
	testImplementation("org.mockito:mockito-inline:5.2.0")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test> {
	useJUnitPlatform()
	jvmArgs("--add-opens=java.base/java.lang=ALL-UNNAMED", "--enable-preview")
}

