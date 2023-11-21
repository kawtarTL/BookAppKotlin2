pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        jcenter()
        maven("https://jitpack.io")
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        jcenter()
        maven("https://jitpack.io")
    }
}

rootProject.name = "BookAppKotlin2"
include(":app")
 