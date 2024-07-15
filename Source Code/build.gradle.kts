plugins {
    java
    application
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web:2.6.4") 
    implementation("mysql:mysql-connector-java:8.0.29")
    testImplementation("org.junit.jupiter:junit-jupiter:5.9.3")
    testImplementation("junit:junit:4.13.1")
    testImplementation("org.mockito:mockito-core:3.12.4")
    testImplementation("org.mockito:mockito-junit-jupiter:3.12.4")
    testImplementation("org.mockito:mockito-inline:4.0.0")
}

application {
    mainClass.set("cafemanagement.MainApplication")
}

tasks.named<JavaExec>("run") {
    standardInput = System.`in`
    args = listOf()
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
-- gradle run --args=""
*/