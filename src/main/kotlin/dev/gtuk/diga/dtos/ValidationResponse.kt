package dev.gtuk.diga.dtos

import com.fasterxml.jackson.annotation.JsonFormat
import java.util.Date

data class ValidationResponse(
    val code: String,
    val digavId: String,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    val dayOfServiceProvision: Date,
    val rawXmlResponse: String
)
