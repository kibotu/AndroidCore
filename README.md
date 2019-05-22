# AndroidCore [![Build Status](https://app.bitrise.io/app/e1099372511a9a9d/status.svg?token=TucfQB9b6iAGlA8faY4F0w&branch=master)](https://app.bitrise.io/app/e1099372511a9a9d) [![](https://jitpack.io/v/exozet/AndroidCore.svg)](https://jitpack.io/#exozet/AndroidCore) [![](https://jitpack.io/v/exozet/AndroidCore.svg/month.svg)](https://jitpack.io/#exozet/AndroidCore) [![Hits-of-Code](https://hitsofcode.com/github/exozet/AndroidCore)](https://hitsofcode.com/view/github/exozet/AndroidCore) [![API](https://img.shields.io/badge/API-15%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=15) [![Gradle Version](https://img.shields.io/badge/gradle-5.4.1-green.svg)](https://docs.gradle.org/current/release-notes)  [![Kotlin](https://img.shields.io/badge/kotlin-1.3.30-green.svg)](https://kotlinlang.org/)  [ ![Download](https://api.bintray.com/packages/exozetag/maven/AndroidCore/images/download.svg) ](https://bintray.com/exozetag/maven/AndroidCore/_latestVersion)

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

    implementation "com.exozet.android:core:$latest"

## Adding as submodule

1. git clone --recursive git@git.exozet.com:exozet-mobile/AndroidCore.git
2. git submodule add git@git.exozet.com:exozet-mobile/AndroidCore.git AndroidCore
3. git commit -m 'Add AndroidCore'
4. git push

## Configuration


#### 1. Add AndroidCore module

by including it into your **${project.rootDir}/settings.gradle** file.

    include ':app', 'core'
    project(':core').projectDir = new File('AndroidCore/core')

#### 2. Update project build.gradle

Copy & paste content 

from **${project.rootDir}/AndroidCore/build.gradle** to **${project.rootDir}/build.gradle**

#### 3. Update app/build.gradle

Copy & paste content 

from **${project.rootDir}/AndroidCore/app/build.gradle** to **${project.rootDir}/app/build.gradle**

Note: Comment out following line if you don't use Google Services:

    apply plugin: 'com.google.gms.google-services'

or otherwise add your google-services.json to your **${project.rootDir}/app/**

#### 4. Update gradle.properties

Copy & paste content  

from **${project.rootDir}/AndroidCore/gradle.properties** to **${project.rootDir}/gradle.properties**

#### 5. Update **${project.rootDir}/app/src/main/AndroidManifest.xml**

Remove following line from application (don't use **allowBackup** unless intented):

    android:allowBackup="true"

#### 6. Update gradle wrapper

by running gradle task **gradle :wrapper**

#### 7. Add CLI support

Enable in AndroidCore the deploy key for your project.

https://git.exozet.com/exozet-mobile/AndroidCore/settings/repository


## Working with submodules

1. commit && push changes to submodule
2. commit && push new commit reference to main project

### Gotchas

- Git doesn't automatically checkout master branch on switching between branches on your main project.
- Git doesn't delete removed or renamed submodules, so after switching branches old submodules may re-appear and want to be re-added. Just delete the old ghost-directory again and make sure to merge submodule changes to all branches.

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


## Google Play Services

### Firebase

https://console.firebase.google.com/u/2/project/androidcore-exozet/settings/general/android:com.exozet.android.core.demo

demo app registered for
user: exozetdev01@googlemail.com

### Notifications

https://console.firebase.google.com/u/2/project/androidcore-exozet/notification/compose

## todo

- location broadcast receiver
- markdown
- rawoutput
- unzip
- fitness
- video player
- bundler
- utils

## Contributors

* [Armando Shkurti](mailto:armando.shkurti@exozet.com)
* [Jan Rabe](mailto:jan.rabe@exozet.com)
