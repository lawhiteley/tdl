package io.whiteley.luke

import io.whiteley.luke.config.TdlPluginExtension
import io.whiteley.luke.listener.HueManipulatingTestListener
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test

open class TdlPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val ext = project.extensions.create(PLUGIN_NAME, TdlPluginExtension::class.java)

        project.tasks.withType(Test::class.java) { task ->
                task.addTestListener(HueManipulatingTestListener(project.logger, ext))
        }
    }

    companion object {
        private const val PLUGIN_NAME: String = "tdl"
    }
}
