pluginManagement {
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

rootProject.name = "Fitbit"
include(
    ":app",
    ":core",
    ":domain",
    ":data",
    ":feature-athlete",
    ":feature-plan",
    ":feature-progress",
    ":feature-dashboard"
)
