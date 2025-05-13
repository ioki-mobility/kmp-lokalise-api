# Lokalise API v2 unofficial KMP client library

[![Tests](https://github.com/ioki-mobility/kmp-lokalise-api/actions/workflows/tests.yml/badge.svg)](https://github.com/ioki-mobility/kmp-lokalise-api/actions/workflows/tests.yml)
[![Maven Central](https://img.shields.io/maven-central/v/com.ioki/lokalise-api?labelColor=%2324292E&color=%233246c8)](https://central.sonatype.com/artifact/com.ioki/lokalise-api) <!-- Disabled because of: https://github.com/badges/shields/pull/10997 [![Snapshot](https://img.shields.io/nexus/s/com.ioki/lokalise-api?labelColor=%2324292E&color=%234f78ff&server=https://s01.oss.sonatype.org)](https://s01.oss.sonatype.org/content/repositories/snapshots/com/ioki/) -->
[![javadoc](https://javadoc.io/badge2/com.ioki/lokalise-api/javadoc.svg?labelColor=%2324292E&color=%236eaaff)](https://javadoc.io/doc/com.ioki/lokalise-api) 
[![MIT](https://img.shields.io/badge/license-MIT-blue.svg?labelColor=%2324292E&color=%23d11064)](https://github.com/ioki-mobility/kmp-lokalise-api/blob/master/LICENSE.md)

Inofficial Kotlin Multiplatform implementation of the [Lokalise API](https://developers.lokalise.com/reference/lokalise-rest-api)
targeting `JVM`, `macOS/X64`, `macOS/Arm64`, `Mingw(Windows)/X64` and `Linux/X64`.

## What?

As we needed an JVM implementation of the Lokalise API in one of our projects 
but didn't find one, we decided to build our own based on KMP.

Even though we focus on JVM 
the other targets has the same priority as the JVM implementation.

## How?

All you need to do is to create an Lokalise API token to create a `Lokalise` instance:

```kotlin
val lokaliseClient = Lokalise("[API_TOKEN]")
```

Optional, you can set `fullLoggingEnabled` to `true` to enable logging for
the HTTP communication.

## Download

### Add the repository

The project is hosted on Maven Central.

```kotlin
repositories {
    mavenCentral()
}
```

### Add the dependency

```kotlin
dependencies {
    implementation("com.ioki:lokalise-api:<latest-version>")
}
```

## Release

### Continuous release

By default, each merge to `main` will create a new `SNAPSHOT` release.
If you want to use the latest and greatest use the `SNAPSHOT` version of the library.
But please be aware that they might contain bugs or behaviour changes.

To use the SNAPSHOT version you have to include the sonatype snapshot repository:

```kotlin
repositories {
    maven(url = "https://central.sonatype.com/repository/maven-snapshots/")
}

dependencies {
    implementation("com.ioki:lokalise-api:<latest-snapshot-version>-SNAPSHOT")
}
```

### Proper release

* Checkout `main`
* Pull latest changes
* Adjust version in [`build.gradle.kts`](build.gradle.kts) to a non-SNAPSHOT version
* Commit `Prepare next release`
* Create a git tag that has the exact name as the version you just modified in the `build.gradle.kts` file.
* Push the `tag`
* Increase the `version` to the next **minor** version +`-SNAPSHOT` (e.g. `0.0.2-SNAPSHOT`) in the [`build.gradle.kts`](build.gradle.kts) file
* Commit `Prepare for next version`
* Push to `main`
* Create a [GitHub release](https://github.com/ioki-mobility/kmp-lokalise-api/releases)

> **Note**: The `tag` push will create a new release with the [`Publish` workflow](https://github.com/ioki-mobility/kmp-lokalise-api/actions/workflows/publish.yml).

