package io.whiteley.luke.service

import io.github.zeroone3010.yahueapi.Hue
import io.github.zeroone3010.yahueapi.Light
import io.github.zeroone3010.yahueapi.Room
import io.github.zeroone3010.yahueapi.State
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import io.whiteley.luke.exception.RoomNotFoundException
import io.whiteley.luke.service.ResultColours.AMBER
import io.whiteley.luke.service.ResultColours.GREEN
import io.whiteley.luke.service.ResultColours.RED
import java.util.Optional
import org.gradle.api.tasks.testing.TestResult.ResultType
import org.gradle.api.tasks.testing.TestResult.ResultType.FAILURE
import org.gradle.api.tasks.testing.TestResult.ResultType.SKIPPED
import org.gradle.api.tasks.testing.TestResult.ResultType.SUCCESS
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource

@ExtendWith(MockKExtension::class)
internal class HueServiceTest {

    @MockK
    private lateinit var mockHue: Hue

    @MockK
    private lateinit var mockRoom: Room

    @RelaxedMockK
    private lateinit var mockLight: Light

    private lateinit var hueService: HueService

    @BeforeEach
    fun setup() {
        every { mockRoom.lights } returns listOf(mockLight)
        every { mockHue.getRoomByName(MOCK_ROOM) } returns Optional.ofNullable(mockRoom)
        hueService = HueService(mockHue)
    }

    @ParameterizedTest
    @EnumSource(value = ResultType::class)
    internal fun `ResultType members should map to the correct colour`(result: ResultType) {
        val expected = mapOf(
                FAILURE to RED,
                SKIPPED to AMBER,
                SUCCESS to GREEN
        )

        hueService.sendResultToRoom(result, MOCK_ROOM)

        verify { mockLight.state = State.builder().color(expected[result]?.hex).on() }
    }

    @Test
    internal fun `RoomNotFoundException is thrown for non-existent rooms`() {
        every { mockHue.getRoomByName(MOCK_ROOM) } returns Optional.empty()

        assertThrows<RoomNotFoundException> {
            hueService.sendResultToRoom(SUCCESS, MOCK_ROOM)
        }
    }

    companion object {
        private const val MOCK_ROOM = "Bedroom"
    }
}
