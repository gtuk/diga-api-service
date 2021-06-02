package dev.gtuk.diga.dtos

import com.alextherapeutics.diga.model.DigaInvoiceMethod
import com.fasterxml.jackson.annotation.JsonProperty

data class BillingResponse(
    val code: String,
    val actionRequired: Boolean,
    val transport: DigaInvoiceMethod,
    @get:JsonProperty("xRechnung")
    val xRechnung: String
)
