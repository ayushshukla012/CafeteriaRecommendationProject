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
*/