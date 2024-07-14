plugins {
    id("java")
    id("application") // Apply the application plugin
}

repositories {
    mavenCentral() // Add this repository to resolve dependencies
}

application {
    mainClass.set("com.cafeteriamanagement.client.AuthClient")
}

dependencies {
    implementation(project(":common"))

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.7.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.7.1")
}

tasks {
    test {
        useJUnitPlatform()
    }
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}
