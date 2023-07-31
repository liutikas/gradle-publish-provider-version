import java.util.Properties
import java.io.StringReader

plugins {
    id("org.jetbrains.kotlin.jvm") version "1.8.20"
    id("maven-publish")
    `java-library`
}

repositories {
    mavenCentral()
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(11))
    }
}

publishing {
    publications {
        create<MavenPublication>("exampleLib") {
            from(components["java"])
        }
    }
    repositories {
        maven {
            name = "exampleRepo"
            url = uri(File(buildDir, "repo"))
        }
    }
}

fun Project.getMyVersion(): Provider<String> {
    val contents = providers.fileContents(
        layout.projectDirectory.file("src/main/resources/version.properties")
    )
    return contents.asText.map {
        val versionProps = Properties()
        versionProps.load(StringReader(it))
        versionProps["version"]!! as String
    }
}

version = getMyVersion()