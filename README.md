# Modern Movies App

## Overview
A simple movies app that fetches movies data using [TheMovieDB API](https://developers.themoviedb.org/3/movies)


<p align="center">
  <img src="https://github.com/SembaMax/modern-movies-app/blob/main/screenshots/modern_movies_app_demo.gif" alt="animated" width="370"/>
</p>



## App
* Min. deployment target Android API 24
* Kotlin source code
* Pagination
* Modular MVVM architecture


## Features to improve/add
- [X] Search Movies


## Screenshots

<p align="center">
<img src="https://github.com/SembaMax/modern-movies-app/blob/main/screenshots/movie_list_light.jpg" width="256" />
&nbsp;
<img src="https://github.com/SembaMax/modern-movies-app/blob/main/screenshots/movie_detail_light.jpg" width="256" />
&nbsp;
</p>
  
<p align="center">
<img src="https://github.com/SembaMax/modern-movies-app/blob/main/screenshots/movie_list_dark.jpg" width="256" />
&nbsp;
<img src="https://github.com/SembaMax/modern-movies-app/blob/main/screenshots/movie_detail_dark.jpg" width="256" />
</p>



## Clean Architecture
The app is designed with multi layer MVVM architecture for better control over individual modules. Which makes the project open for scalability, and flexible to change, maintain and test.


## How to pull the repository

	git clone git@github.com:SembaMax/modern-movies-app.git
  

## Build Requirements

- JDK11
- Supports minimum API 24
- Gradle 7.5.1+
- Kotlin 1.7.21+
- [Android Studio Electric Eel](https://developer.android.com/studio/)

## Configuration

1. Login into [TheMovieDB](https://www.themoviedb.org/) for getting API_KEY and ACCESS_TOKEN
2. Add `API_KEY="Your_Api_Key"` to `gradle.properties` file.
3. Add `ACCESS_TOKEN="Your_Access_Token"` to `gradle.properties` file.

```kotlin
  defaultConfig {
        buildConfigField("String", "API_KEY", properties.getProperty("API_KEY"))
        buildConfigField("String", "ACCESS_TOKEN", properties.getProperty("ACCESS_TOKEN"))
    }
 ```


## Features

- Jetpack Compose
- Compose Navigation
- Material Design 3
- Themes
- Flows
- Retrofit2
- Timber
- Hilt
- Coroutines
- Coil
- Gradle kts
- Modularization
