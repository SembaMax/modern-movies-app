pluginManagement {
    //includeBuild("build-logic")
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "ModernMoviesApp"
include(":app")
include(":core:common")
include(":core:design")
include(":core:testing")
include(":data:model")
include(":data:remote")
include(":data:repository")
include(":feature:moviedetail")
include(":feature:movielist")
