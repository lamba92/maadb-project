import com.github.lamba92.gradle.utils.TRAVIS_TAG

allprojects {
    group = "com.github.lamba92"
    version = TRAVIS_TAG ?: "1.0.8"
}

subprojects {
    repositories {
        maven("https://jitpack.io")
        mavenCentral()
        jcenter()
        maven("https://dl.bintray.com/lamba92/com.github.lamba92")
    }
}
