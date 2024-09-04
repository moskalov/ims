plugins {
    id("idea")
    id("java")
    id("org.springframework.boot") version "3.3.3"
    id("io.spring.dependency-management") version "1.1.6"
}

group = "lv.notes"
version = "0.0.1-SNAPSHOT"

repositories {
    mavenCentral()
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

sourceSets {
    create("integrationTest") {
        java.srcDir(file("$projectDir/src/integrationTest/java"))
        compileClasspath += sourceSets.main.get().output + sourceSets.test.get().output
        runtimeClasspath += sourceSets.main.get().output + sourceSets.test.get().output
    }
    create("e2eTest") {
        java.srcDir(file("$projectDir/src/e2eTest/java"))
        compileClasspath += sourceSets.main.get().output + sourceSets.test.get().output
        runtimeClasspath += sourceSets.main.get().output + sourceSets.test.get().output
    }
}

configurations {
    listOf(
        configurations["e2eTestAnnotationProcessor"] to configurations["annotationProcessor"],
        configurations["e2eTestImplementation"] to configurations["testImplementation"],
        configurations["e2eTestRuntimeOnly"] to configurations["testRuntimeOnly"],
        configurations["e2eTestCompileOnly"] to configurations["compileOnly"],
        configurations["integrationTestAnnotationProcessor"] to configurations["annotationProcessor"],
        configurations["integrationTestImplementation"] to configurations["testImplementation"],
        configurations["integrationTestRuntimeOnly"] to configurations["testRuntimeOnly"],
        configurations["integrationTestCompileOnly"] to configurations["compileOnly"]
    ).forEach { (config, parent) ->
        config.extendsFrom(parent)
    }
}

dependencies {
    sourceSets.named("e2eTest") {
        implementation("io.rest-assured:rest-assured:5.5.0")
        implementation("io.rest-assured:json-path:5.5.0")
    }
    sourceSets.named("test") {
        implementation("org.springframework.boot:spring-boot-starter-test")
        runtimeOnly("org.junit.platform:junit-platform-launcher")
    }
    sourceSets.named("main") {
        implementation("org.springframework.boot:spring-boot-starter-validation:3.3.3")
        implementation("org.springframework.boot:spring-boot-starter-data-jpa")
        implementation("org.springframework.boot:spring-boot-starter-web")
        implementation("org.mapstruct:mapstruct:1.6.0")
        annotationProcessor("org.projectlombok:lombok")
        annotationProcessor("org.mapstruct:mapstruct-processor:1.6.0")
        compileOnly("org.projectlombok:lombok")
        runtimeOnly("com.h2database:h2")
    }
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}

val integrationTest = task<Test>("integrationTest") {
    testClassesDirs = sourceSets["integrationTest"].output.classesDirs
    classpath = sourceSets["integrationTest"].runtimeClasspath
    description = "Runs integration tests."
    group = "verification"
    useJUnitPlatform()
}

val e2eTest = task<Test>("e2eTest") {
    testClassesDirs = sourceSets["e2eTest"].output.classesDirs
    classpath = sourceSets["e2eTest"].runtimeClasspath
    description = "Runs e2e tests."
    group = "verification"
    useJUnitPlatform()
}

idea {
    module {
        testSources.from("src/integrationTest/java", "src/e2eTest/java")
    }
}