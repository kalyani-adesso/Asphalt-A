# Asphalt-A KMP Project

## Project Overview

A Kotlin Multiplatform Mobile (KMP) application for Motorcycle and Bike Riders during their group rides and trips. 
This project provides a unified codebase for Android and iOS platforms with shared business logic and platform-specific implementations.

Asphalt-A is a mobile app designed to help motorcycle and bike riders during their group rides and trips.  
The application includes a variety of features that help riders stay connected, share knowledge, and test their skills.  

## Architecture

- androidApp -> shared (KMP + Native)
- iOSApp -> shared (KMP + Native)
- androidApp -> feature Modules (KMP) -> shared (KMP + Native)

## Module Structure

- androidApp/ - Android-specific application module
- iosApp/ - iOS-specific application module (Xcode project)
- shared/ - Shared Kotlin Multiplatform code
- feature/ - Feature modules organized by functionality

🛠️ Technology Stack
## Core Technologies

- Language: Kotlin, Java
- Build System: Gradle with Version Catalogs
- Architecture: MVVM with StateFlow
- Platforms: Android, iOS (Kotlin Multiplatform Mobile)

## UI & Design

- UI Framework: Jetpack Compose
- Design System: Material3
- Navigation: Navigation 3
- Responsive Design: Adaptive layouts for different screen sizes

## Backend & Data

- API: KtorClient for backend communication
- Local Storage: Room with SQLCipher for encrypted database
- Caching: In-memory caching with expiration strategies
- Preferences: DataStore for user preferences

## Testing & Quality

- Unit Tests: Common tests in commonTest, platform-specific tests
- UI Tests: Compose UI testing framework
- Mocking: MockK for Kotlin mocking
- Code Quality: Detekt for static analysis
- Coverage: Kover for code coverage analysis

## Dependency Injection & Async

- DI Framework: Koin for dependency injection
- Coroutines: kotlinx-coroutines for asynchronous operations
- State Management: StateFlow for reactive UI updates

🔧 Development Setup
## Prerequisites

- JDK: OpenJDK 21 or later
- Android Studio: Latest stable version with KMP plugin
- Xcode: Latest version (for iOS development)
- Android SDK: API 26+ (Android 8.0+)

## Features

### Rides
- Create rides  
- Join rides  
- Create and manage ride groups  

### Connected Riding
- Stay connected with your group during rides  
- Supports wearable devices for seamless experience  

### Knowledge Circle
- Increase and test your knowledge  
- Share insights with other riders  

### Queries
- Ask questions  
- Solve and answer queries from fellow riders and pros  

### Moto Quiz
- Test your knowledge  
- Challenge your friends  
