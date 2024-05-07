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
        maven {
            url = uri("https://jitpack.io/com/github/smarteist/autoimageslider/1.4.0/autoimageslider-1.4.0.pom")
        }
        maven {
            url = uri("https://jitpack.io")
        }
    }
}

rootProject.name = "HomeServicesDemo"
include(":app")
