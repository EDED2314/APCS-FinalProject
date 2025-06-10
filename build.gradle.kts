plugins {
    id("java")
}

group = "org.example"
version = "1.0"

repositories {
    mavenCentral()
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(11)) // Uses JDK 21
    }
}

dependencies {
   // implementation("org.apache.commons:commons-lang3:3.5")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")

}

tasks.jar{
    manifest.attributes["Main-Class"] = "project.MainGameContainer"
}

tasks.test {
    useJUnitPlatform()
}