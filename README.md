# Android Common Utils [![Build Status](https://www.bitrise.io/app/e1099372511a9a9d/status.svg?token=TucfQB9b6iAGlA8faY4F0w&branch=develop)](https://www.bitrise.io/app/e1099372511a9a9d) [![API](https://img.shields.io/badge/API-15%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=15) [![Gradle Version](https://img.shields.io/badge/gradle-4.2.1-green.svg)](https://docs.gradle.org/current/release-notes) 
## Table of contents
* [Introduction](#introduction)
* [Redmine](https://redmine.exozet.com/projects/hi-gothaer)
* [Wiki](https://redmine.exozet.com/projects/hi-gothaer/wiki)
* [Distribution](#distribution)
* [Continues Integration](#continues-integration)
* [Bugtracking](#bugtracking) 	
* [How to install](#how-to-install)
    * [Google Play](#using-google-play) 
    * [Hockey App](#using-hockey-app) 
    * [Android Debug Bridge](#using-android-debug-bridge)
    * [Gradle](#using-gradle) 
* [Keystores](#keystores)
* [How to build](#how-to-build)
* [How to upload to hockey](#how-to-upload-to-hockey)
* [Related](#also-see)

## Introduction

Client project for AndroidCommonUtils. Insert Text here!!!

## Distribution

### Debug

[Public Page](https://rink.hockeyapp.net/apps/6182d7190ab34087982bd64ae4c74e2f)


[![QRCode](https://chart.googleapis.com/chart?cht=qr&chl=https%3A%2F%2Frink.hockeyapp.net%2Fapps%2F6182d7190ab34087982bd64ae4c74e2f&chs=256x256)](https://chart.googleapis.com/chart?cht=qr&chl=https%3A%2F%2Frink.hockeyapp.net%2Fapps%2F6182d7190ab34087982bd64ae4c74e2f&chs=256x256)

### Release

[Public Page](https://rink.hockeyapp.net/apps/34d99082996c427e86bc0629bf289b19)


[![QRCode](https://chart.googleapis.com/chart?cht=qr&chl=https%3A%2F%2Frink.hockeyapp.net%2Fapps%2F34d99082996c427e86bc0629bf289b19&chs=256x256)](https://chart.googleapis.com/chart?cht=qr&chl=https%3A%2F%2Frink.hockeyapp.net%2Fapps%2F34d99082996c427e86bc0629bf289b19&chs=256x256)

## Continues Integration

[Bitrise](https://www.bitrise.io/app/0846afc5e4510f64#/builds)

## Bugtracking

### Debug

[Fabric](https://fabric.io/exozet4/android/apps/de.gothaer.hi.debug/issues?time=last-seven-days&event_type=all&subFilter=state&state=open&cohort=new)

### Release

[Fabric](https://fabric.io/exozet4/android/apps/de.gothaer.hi/issues?time=last-seven-days&event_type=all&subFilter=state&state=open&cohort=new)

## How to install
### Using Google Play
[Playstore](https://www.google.com)
### Using Hockey App 
[See Distribution](#distribution)
### Using Android Debug Bridge
#### Debug
	adb install app/build/outputs/apk/app-release.apk
#### Release
    adb install app/build/outputs/apk/app-release.apk
### Using Gradle
#### Debug
	./gradlew installDebug
#### Release
    ./gradlew installRelease
    
## Keystores
[Mobile Provisioning](https://git.exozet.com/exozet/mobile-provisioning/tree/master/Gothaer/Hi%20At%20Gothaer)
## How to build

0. (optional) requires java 7 and java 8 installation for [retrolambda](https://github.com/evant/gradle-retrolambda) @see [Additional Installation Notes](#additional-installation-notes)

1. clone project

		git clone git@git.exozet.com:gothaer/Hi-At-Gothaer-Android.git

2. build using gradle (Note: requires gradle >= 3.3, java 1.8 and android build tools >= 25 installed)

		gradle clean build 
    
## How to upload to hockey
### Debug
#### Bitrise
Bitrise will automatically upload debug to hockey app if something get changed in the development branch on git.
#### Manually
	gradle uploadDebugToHockeyApp
 
### Release
#### Bitrise
Bitrise will automatically upload release to hockey app if something get changed, in one of the release branches on git.
#### Manually
	gradle uploadReleaseToHockeyApp    
  
## Contributors

* [Paul Sprotte](mailto:paul.sprotte@exozet.com)
* [Jan Rabe](mailto:jan.rabe@exozet.com)  
  
## Also see

iOS Version MISSING
    
    
## Additional Installation Notes

1. Install Homebrew

        ruby -e "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/master/install)"
     
2. Install Java with cask

        brew tap caskroom/versions
        brew cask install java7      
                   

3. Install android sdk
    
        brew install android-sdk

4. Set android home

        export ANDROID_HOME="/usr/local/opt/android-sdk"
         
5. Install android api level and extras

        android sdk 
    
6. (Bonus) updates in the future:
        
        brew update
        brew upgrade