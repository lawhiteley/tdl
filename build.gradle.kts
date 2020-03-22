plugins {
    `java-gradle-plugin`
    `maven-publish`

    id("com.gradle.plugin-publish") version "0.10.1"
    id("org.jetbrains.kotlin.jvm") version "1.3.61"
    id("org.jlleitschuh.gradle.ktlint") version "9.2.1"
}

repositories {
    jcenter()
}

dependencies {
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("io.github.zeroone3010:yetanotherhueapi:1.3.1")

    testImplementation("io.mockk:mockk:1.9.3")
    testImplementation("org.junit.jupiter:junit-jupiter:5.6.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.6.0")
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }

    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }

    "test"(Test::class) {
        useJUnitPlatform()
    }
}

gradlePlugin {
    plugins {
        create("tdl") {
            id = "io.whiteley.luke.tdl"
            displayName = "tdl"
            description = "Reflects Gradle test results (Red/Green) in a given Philips Hue room"
            implementationClass = "io.whiteley.luke.TdlPlugin"
        }
    }
}

pluginBundle {
    website = "https://github.com/lawhiteley/tdl"
    vcsUrl = "https://github.com/lawhiteley/tdl"
    tags = listOf("iot", "philips", "hue", "kotlin", "testing", "tdd", "gradle")
}

group = "io.whiteley.luke"
version = "1.0.0"
