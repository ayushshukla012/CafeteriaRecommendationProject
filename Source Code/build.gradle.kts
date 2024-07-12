plugins {
    id("java")
}

allprojects {
    group = "com.cafeteriamanagement"
    version = "1.0-SNAPSHOT"

    repositories {
        mavenCentral()
    }
}

subprojects {
    apply(plugin = "java")

    repositories {
        mavenCentral()
    }

    dependencies {
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
}
