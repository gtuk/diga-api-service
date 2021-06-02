package dev.gtuk.diga.dtos

import java.util.Date
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.Size

data class BillingRequest(
    @get:NotEmpty @get:Size(min = 16, max = 16) val code: String,
    @get:NotEmpty @get:Size(min = 8, max = 8) val digavId: String,
    val dayOfServiceProvision: Date,
    @get:NotEmpty val invoiceNumber: String
)
