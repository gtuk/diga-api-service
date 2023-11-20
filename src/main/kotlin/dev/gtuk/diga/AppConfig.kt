package dev.gtuk.diga

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.Size
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.validation.annotation.Validated
import java.math.BigDecimal


@ConfigurationProperties("app")
@Validated
data class AppConfig(
        val disableTestcodes: Boolean,
        @get:NotEmpty val testInsurance: String,
        @get:NotEmpty val mappingFile: String,
        @get:NotEmpty val healthCompaniesKeystore: String,
        @get:NotEmpty val privateKeystore: String,
        @get:NotEmpty val privateKeystorePassword: String,
        @get:NotEmpty val healthCompaniesKeystorePassword: String,
        @get:NotEmpty val privateKeyAlias: String,
        @get:NotEmpty val digaName: String,
        @get:NotEmpty @get:Size(min = 5, max = 5) val digaId: String,
        @get:NotEmpty val companyName: String,
        @get:NotEmpty @get:Size(min = 9, max = 9) val companyIk: String,
        @get:NotEmpty val companyVatNumber: String,
        val netPrice: BigDecimal,
        val vatPercent: BigDecimal,
        @get:NotEmpty val contactFullName: String,
        @get:NotEmpty val contactPhoneNumber: String,
        @get:NotEmpty @get:Email val contactEmailAddress: String,
        @get:NotEmpty val companyAddress: String,
        @get:NotEmpty val companyZipCode: String,
        @get:NotEmpty val companyCity: String,
        @get:NotEmpty val companyCountryCode: String
)
