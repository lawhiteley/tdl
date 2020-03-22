package io.whiteley.luke.service

import io.github.zeroone3010.yahueapi.Hue
import io.github.zeroone3010.yahueapi.State
import io.whiteley.luke.exception.RoomNotFoundException
import io.whiteley.luke.service.ResultColours.AMBER
import io.whiteley.luke.service.ResultColours.GREEN
import io.whiteley.luke.service.ResultColours.RED
import org.gradle.api.tasks.testing.TestResult.ResultType

open class HueService(private val hue: Hue) {

    fun sendResultToRoom(result: ResultType, roomName: String) {
        val resultAsColour = when (result) {
            ResultType.FAILURE -> RED
            ResultType.SUCCESS -> GREEN
            else -> AMBER
        }

        hue.getRoomByName(roomName).map {
            it.lights.map { light -> light.state = State.builder().color(resultAsColour.hex).on() }
        }.orElseThrow { RoomNotFoundException("Unable to find room '$roomName'") }
    }
}

enum class ResultColours(val hex: String) {
    RED("FF0000"),
    AMBER("FFBF00"),
    GREEN("00FF00")
}
