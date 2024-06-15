plugins {
    java
    application
}

repositories {
    mavenCentral()
}

dependencies {
    // Example dependencies
    implementation("org.springframework.boot:spring-boot-starter-web:2.6.4") 
    implementation("mysql:mysql-connector-java:8.0.29")
    testImplementation("org.junit.jupiter:junit-jupiter:5.9.3")
}

// java {
//     toolchain {
//         languageVersion.set(JavaLanguageVersion.of(8))
//     }
// }

application {
    // Define the main class for the application.
    mainClass.set("cafemanagement.MainApplication")
}

tasks.named<JavaExec>("run") {
    standardInput = System.`in`
}

tasks.named<Test>("test") {
    // Use JUnit Platform for unit tests.
    useJUnitPlatform()
}


/* 
To run
-- gradle --console plain run
-- gradle build
-- gradle run
W*/