# Lokalise API v2 unofficial KMP client library

[![Tests](https://github.com/ioki-mobility/kmp-lokalise-api/actions/workflows/tests.yml/badge.svg)](https://github.com/ioki-mobility/kmp-lokalise-api/actions/workflows/tests.yml)

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

The project is hosted on [GitHub Packages](https://github.com/orgs/ioki-mobility/packages?repo_name=kmp-lokalise-api).

```kotlin
repositories {
    maven(url = "https://ghpkgs.cloud/ioki-mobility/kmp-lokalise-api")
}
```

### Add the dependency

```kotlin
dependencies {
    implementation("com.ioki:lokalise-api:<latest-version>")
}
```