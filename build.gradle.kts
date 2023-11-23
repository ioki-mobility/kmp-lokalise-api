plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.kotlinDokka)
    `maven-publish`
}

repositories {
    mavenCentral()
}

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

    macosArm64()
    macosX64()
    linuxX64()
    mingwX64()

    sourceSets {
        commonMain.dependencies {
            implementation(libs.common.ktor.core)
            implementation(libs.common.ktor.contentNegotiation)
            implementation(libs.common.ktor.serialization)
            implementation(libs.common.ktor.logging)
        }
        commonTest.dependencies {
            implementation(libs.common.test.kotlin)
            implementation(libs.common.test.ktorMock)
        }
        jvmMain.dependencies {
            implementation(libs.jvm.ktor.client)
            implementation(libs.jvm.ktor.logging)
        }
        macosMain.dependencies {
            implementation(libs.macos.ktor.client)
        }
        linuxMain.dependencies {
            implementation(libs.linux.ktor.client)
        }
        mingwMain.dependencies {
            implementation(libs.mingw.ktor.client)
        }
    }
}

val dokkaJar = tasks.register<Jar>("dokkaJar") {
    dependsOn(tasks.dokkaHtml)
    from(tasks.dokkaHtml.flatMap { it.outputDirectory })
    archiveClassifier.set("javadoc")
}

group = "com.ioki"
version = "0.0.2-SNAPSHOT"
publishing {
    publications {
        publications.withType<MavenPublication> {
            artifact(dokkaJar)
            artifactId = artifactId.replace("kmp-", "")

            pom {
                name.set("KMP Lokalise Api")
                description.set("Lokalise API v2 Kotlin Multiplatform client library")
                url.set("https://github.com/ioki-mobility/kmp-lokalise-api")

                licenses {
                    license {
                        name.set("MIT")
                        url.set("https://opensource.org/licenses/MIT")
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
                        organizationUrl.set("https://ioki.com")
                    }
                }
                scm {
                    url.set("https://github.com/ioki-mobility/kmp-lokalise-api")
                    connection.set("scm:git:git://github.com/ioki-mobility/kmp-lokalise-api.git")
                    developerConnection.set("scm:git:ssh://git@github.com/ioki-mobility/kmp-lokalise-api.git")
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
