package dev.gtuk.diga.dtos

import com.fasterxml.jackson.annotation.JsonFormat
import java.util.Date

data class ValidationErrorResponse(
    val status: Int,
    var errorCode: String,
    var errorText: String,
    val path: String
) {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM-dd-yyyy hh:mm:ss")
    var timestamp: Date = Date()

    init {
        this.timestamp = Date()
    }
}
