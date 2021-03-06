package io.whiteley.luke.listener

import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.verify
import io.whiteley.luke.config.TdlPluginExtension
import io.whiteley.luke.exception.BridgeConnectionException
import io.whiteley.luke.service.HueService
import org.gradle.api.logging.Logger
import org.gradle.api.tasks.testing.TestDescriptor
import org.gradle.api.tasks.testing.TestResult
import org.gradle.api.tasks.testing.TestResult.ResultType.SUCCESS
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
internal class HueManipulatingTestListenerTest {

    @RelaxedMockK
    private lateinit var logger: Logger

    @MockK
    private lateinit var result: TestResult

    @MockK
    private lateinit var suite: TestDescriptor

    @RelaxedMockK
    private lateinit var hueService: HueService

    private lateinit var ext: TdlPluginExtension

    private lateinit var testListener: HueManipulatingTestListener

    @BeforeEach
    fun eachTest() {
        every { suite.parent } returns null
        ext = TdlPluginExtension(MOCK_ROOM, MOCK_IP, MOCK_API_KEY)
        testListener = HueManipulatingTestListener(logger, ext, hueService)
    }

    @Test
    fun `Result is sent to specified room after all suites have been run`() {
        val expectedResult = SUCCESS
        every { result.resultType } returns expectedResult

        testListener.afterSuite(suite, result)

        verify { hueService.sendResultToRoom(expectedResult, ext.roomName!!) }
    }

    @Test
    fun `HueService is not called if there are still suites to be run`() {
        every { suite.parent } returns mockk()

        testListener.afterSuite(suite, result)

        verify(exactly = 0) { hueService.sendResultToRoom(any(), any()) }
    }

    @Test
    fun `HueService exceptions are caught and logged`() {
        every { result.resultType } returns SUCCESS
        every { hueService.sendResultToRoom(result.resultType, MOCK_ROOM) } throws BridgeConnectionException("test")

        testListener.afterSuite(suite, result)

        verify { logger.error(any()) }
    }

    @Test
    fun `Error is logged if hueApiKey is not supplied`() {
        ext.hueApiKey = null

        testListener.afterSuite(suite, result)

        verify { logger.error(CONFIG_ERROR) }
    }

    @Test
    fun `Error is logged if bridgeIp is not supplied`() {
        ext.bridgeIp = null

        testListener.afterSuite(suite, result)

        verify { logger.error(CONFIG_ERROR) }
    }

    companion object {
        private const val MOCK_IP = "127.0.0.1"
        private const val MOCK_ROOM = "test-room"
        private const val MOCK_API_KEY = "12345"
        private const val CONFIG_ERROR = "tdl failed: Both hueApiKey and bridgeIp must be supplied"
    }
}
