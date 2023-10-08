plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.kotlinSerialization)
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
        val commonMain by getting {
            dependencies {
                implementation(libs.common.ktor.core)
                implementation(libs.common.ktor.contentNegotiation)
                implementation(libs.common.ktor.serialization)
                implementation(libs.common.ktor.logging)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(libs.common.test.kotlin)
                implementation(libs.common.test.ktorMock)
            }
        }
        val jvmMain by getting {
            dependencies {
                implementation(libs.jvm.ktor.client)
                implementation(libs.jvm.ktor.logging)
            }
        }
        val jvmTest by getting
        val nativeMain by getting {
            dependencies {
                when {
                    hostOs == "Mac OS X" -> implementation(libs.macos.ktor.client)
                    hostOs == "Linux" -> implementation(libs.linux.ktor.client)
                    isMingwX64 -> implementation(libs.mingw.ktor.client)
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
                        name.set("MIT")
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
