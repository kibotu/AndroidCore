# AndroidCore [ ![Download](https://api.bintray.com/packages/exozetag/maven/AndroidCore/images/download.svg) ](https://bintray.com/exozetag/maven/AndroidCore/_latestVersion) [![Build Status](https://www.bitrise.io/app/e1099372511a9a9d/status.svg?token=TucfQB9b6iAGlA8faY4F0w&branch=master)](https://www.bitrise.io/app/e1099372511a9a9d) [![API](https://img.shields.io/badge/API-15%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=15) [![Gradle Version](https://img.shields.io/badge/gradle-4.5-green.svg)](https://docs.gradle.org/current/release-notes) [![Kotlin](https://img.shields.io/badge/kotlin-1.2.21-green.svg)](https://kotlinlang.org/)
## Table of contents
* [Introduction](#introduction)
* [How to install](#how-to-install)
* [Continues Integration](#continues-integration)
* [Bugtracking](#bugtracking)
* [Sample App](#Sample-App)
* [Contributors](#Contributors)

## Introduction

Collection of re-usable android functions.

## How to install

    implementation 'com.exozet.android:core:latest'

## Continues Integration

[Bitrise](https://www.bitrise.io/app/e1099372511a9a9d#/builds)

    gradlew clean build

## Bug tracking

[Gitlab Issues](https://git.exozet.com/exozet-mobile/AndroidCore/issues)

## Sample App

    adb -s <device_name> install <apk_file>.apk

## Features

### Receiver

    for tracking incoming and outgoing phone calls
    PhoneCallReceiver

    for tracking SMS
    SmsReceiver

## Contributors

* [Armando Shkurti](mailto:armando.shkurti@exozet.com)
* [Jan Rabe](mailto:jan.rabe@exozet.com)