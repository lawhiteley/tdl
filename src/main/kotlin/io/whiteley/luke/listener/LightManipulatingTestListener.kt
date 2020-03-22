package io.whiteley.luke.listener

import org.gradle.api.tasks.testing.TestDescriptor
import org.gradle.api.tasks.testing.TestListener
import org.gradle.api.tasks.testing.TestResult

abstract class LightManipulatingTestListener : TestListener {
    override fun beforeSuite(suite: TestDescriptor?) {}
    override fun beforeTest(p0: TestDescriptor?) {}
    override fun afterTest(p0: TestDescriptor?, p1: TestResult?) {}
}
