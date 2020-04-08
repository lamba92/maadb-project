@file:Suppress("unused")

import com.jfrog.bintray.gradle.BintrayExtension
import org.gradle.api.Action
import org.gradle.api.Project
import org.gradle.api.publish.PublishingExtension
import org.gradle.kotlin.dsl.closureOf
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinDependencyHandler

fun KotlinDependencyHandler.ktor(module: String, version: String? = null): Any =
    "io.ktor:ktor-$module${version?.let { ":$version" } ?: ""}"

fun KotlinDependencyHandler.lamba(module: String, version: String? = null): Any =
    "com.github.lamba92:$module${version?.let { ":$version" } ?: ""}"

val Project.kotlin
    get() = extensions.getByType<KotlinMultiplatformExtension>()

fun Project.kotlin(action: KotlinMultiplatformExtension.() -> Unit) =
    extensions.configure(action)

val Project.publishing
    get() = extensions.getByType<PublishingExtension>()

fun Project.bintray(action: BintrayExtension.() -> Unit) =
    extensions.configure(action)

val Project.bintray
    get() = extensions.getByType<BintrayExtension>()

fun Project.publishing(action: PublishingExtension.() -> Unit) =
    extensions.configure(action)

fun Project.searchPropertyOrNull(propertyName: String): String? =
    findProperty(propertyName) as String? ?: System.getenv(propertyName)

fun BintrayExtension.pkg(action: BintrayExtension.PackageConfig.() -> Unit) {
    pkg(closureOf(action))
}

fun BintrayExtension.PackageConfig.version(action: BintrayExtension.VersionConfig.() -> Unit) {
    version(closureOf(action))
}

fun KotlinDependencyHandler.kotlinx(module: String, version: String? = null, prefix: Boolean = true): Any =
    "org.jetbrains.kotlinx:${if (prefix) "kotlinx-" else ""}$module${version?.let { ":$version" } ?: ""}"

fun KotlinDependencyHandler.serialization(module: String, version: String? = null): Any =
    "org.jetbrains.kotlinx:kotlinx-serialization-$module${version?.let { ":$version" } ?: ""}"

@Suppress("ObjectPropertyName")
val `TRAVIS-TAG`
    get() = System.getenv("TRAVIS_TAG").run {
        if (isNullOrBlank()) null else this
    }

fun <T> Action<T>.asLambda(): T.() -> Unit = {
    execute(this)
}

fun BintrayExtension.setPublications(names: Iterable<String>) =
    setPublications(*names.toList().toTypedArray())
