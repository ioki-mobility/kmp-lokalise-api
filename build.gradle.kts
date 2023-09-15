plugins {
    kotlin("multiplatform") version "1.9.10"
    kotlin("plugin.serialization") version "1.9.10"
    `maven-publish`
}

repositories {
    mavenCentral()
}

val hostOs = System.getProperty("os.name")
val isArm64 = System.getProperty("os.arch") == "aarch64"
val isMingwX64 = hostOs.startsWith("Windows")
kotlin {
    jvm {
        jvmToolchain(8)
        withJava()
        testRuns.named("test") {
            executionTask.configure {
                useJUnitPlatform()
            }
        }
    }

    when {
        hostOs == "Mac OS X" && isArm64 -> macosArm64("native")
        hostOs == "Mac OS X" && !isArm64 -> macosX64("native")
        hostOs == "Linux" && !isArm64 -> linuxX64("native")
        isMingwX64 -> mingwX64("native")
        else -> throw GradleException("Host OS is not supported in Kotlin/Native.")
    }

    sourceSets {
        val ktorVersion = "2.3.4"
        val commonMain by getting {
            dependencies {
                implementation("io.ktor:ktor-client-core:$ktorVersion")
                implementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")
                implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")
                implementation("io.ktor:ktor-client-logging:$ktorVersion")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation("io.ktor:ktor-client-mock:$ktorVersion")
            }
        }
        val jvmMain by getting {
            dependencies {
                implementation("ch.qos.logback:logback-classic:1.3.0")
                implementation("io.ktor:ktor-client-okhttp:$ktorVersion")
            }
        }
        val jvmTest by getting
        val nativeMain by getting {
            dependencies {
                when {
                    hostOs == "Mac OS X" -> implementation("io.ktor:ktor-client-darwin:$ktorVersion")
                    hostOs == "Linux" -> implementation("io.ktor:ktor-client-curl:$ktorVersion")
                    isMingwX64 -> implementation("io.ktor:ktor-client-winhttp:$ktorVersion")
                    else -> throw GradleException("Host OS is not supported in Kotlin/Native.")
                }

            }
        }
        val nativeTest by getting
    }
}

group = "com.ioki"
version = "0.0.1-SNAPSHOT"
publishing {
    publications {
        publications.withType<MavenPublication> {
            artifactId = when {
                hostOs == "Mac OS X" && isArm64 -> artifactId.replace("native", "macosarm64")
                hostOs == "Mac OS X" && !isArm64 -> artifactId.replace("native", "macosx64")
                hostOs == "Linux" && !isArm64 -> artifactId.replace("native", "linuxx64")
                isMingwX64 -> artifactId.replace("native", "mingwx64")
                else -> throw GradleException("Host OS is not supported in Kotlin/Native.")
            }.run { replace("kmp-", "") }
            pom {
                url.set("https://github.com/ioki-mobility/kmp-lokalise-api")
                licenses {
                    license {
                        name.set("MIT License")
                        url.set("https://github.com/ioki-mobility/kmp-lokalise-api/blob/main/LICENSE")
                    }
                }
                organization {
                    name.set("ioki")
                    url.set("https://ioki.com")
                }
                developers {
                    developer {
                        name.set("Stefan 'StefMa' M.")
                        email.set("StefMaDev@outlook.com")
                        url.set("https://StefMa.guru")
                        organization.set("ioki")
                    }
                }
                scm {
                    url.set("https://github.com/ioki-mobility/kmp-lokalise-api")
                    connection.set("https://github.com/ioki-mobility/kmp-lokalise-api.git")
                    developerConnection.set("git@github.com:ioki-mobility/kmp-lokalise-api.git")
                }
            }
        }
    }

    repositories {
        maven("https://maven.pkg.github.com/ioki-mobility/kmp-lokalise-api") {
            name = "GitHubPackages"
            credentials {
                username = project.findProperty("githubPackagesUser") as? String
                password = project.findProperty("githubPackagesKey") as? String
            }
        }
    }
}
