package io.whiteley.luke.exception

import java.lang.RuntimeException

open class RoomNotFoundException(message: String?) : RuntimeException(message)
