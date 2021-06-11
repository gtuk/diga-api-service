package dev.gtuk.diga

import com.alextherapeutics.diga.DigaApiClient
import com.alextherapeutics.diga.model.DigaApiClientSettings
import com.alextherapeutics.diga.model.DigaApiResponseError
import com.alextherapeutics.diga.model.DigaCodeValidationResponse
import com.alextherapeutics.diga.model.DigaInformation
import com.alextherapeutics.diga.model.DigaInvoice
import com.alextherapeutics.diga.model.DigaInvoiceResponse
import dev.gtuk.diga.dtos.BillingRequest
import dev.gtuk.diga.dtos.BillingResponse
import dev.gtuk.diga.dtos.ValidationResponse
import dev.gtuk.diga.exceptions.BillingException
import dev.gtuk.diga.exceptions.CodeValidationException
import dev.gtuk.diga.exceptions.ValidationException
import java.io.FileInputStream
import kotlin.jvm.Throws
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class DigaService(private val appConfig: AppConfig) {

    private val logger = LoggerFactory.getLogger(javaClass)

    private final var apiClient: DigaApiClient

    init {
        val apiClientSettings = DigaApiClientSettings.builder()
            .healthInsuranceMappingFile(FileInputStream(appConfig.mappingFile))
            .privateKeyStoreFile(FileInputStream(appConfig.privateKeystore))
            .healthInsurancePublicKeyStoreFile(FileInputStream(appConfig.healthCompaniesKeystore))
            .privateKeyStorePassword(appConfig.privateKeystorePassword)
            .privateKeyAlias(appConfig.privateKeyAlias)
            .healthInsurancePublicKeyStorePassword(appConfig.healthCompaniesKeystorePassword)
            .build()

        val digaInformation =
            DigaInformation.builder()
                .digaName(appConfig.digaName)
                .digaId(appConfig.digaId)
                .manufacturingCompanyName(appConfig.companyName)
                .manufacturingCompanyIk(appConfig.companyIk)
                .netPricePerPrescription(appConfig.netPrice)
                .applicableVATpercent(appConfig.vatPercent)
                .manufacturingCompanyVATRegistration(appConfig.companyVatNumber)
                .contactPersonForBilling(
                    DigaInformation.ContactPersonForBilling.builder()
                        .fullName(appConfig.contactFullName)
                        .phoneNumber(appConfig.contactPhoneNumber)
                        .emailAddress(appConfig.contactEmailAddress)
                        .build()
                )
                .companyTradeAddress(
                    DigaInformation.CompanyTradeAddress.builder()
                        .adressLine(appConfig.companyAddress)
                        .postalCode(appConfig.companyZipCode)
                        .city(appConfig.companyCity)
                        .countryCode(appConfig.companyCountryCode)
                        .build()
                )
                .build()

        this.apiClient = DigaApiClient(apiClientSettings, digaInformation)
    }

    private fun validateCode(code: String) {
    }

    @Throws(ValidationException::class)
    fun verify(code: String): ValidationResponse {
        val isTestCode = Utils.isTestCode(code)

        val response: DigaCodeValidationResponse = try {
            if (isTestCode && appConfig.disableTestcodes) {
                throw CodeValidationException("Testcodes are not allowed")
            } else if (isTestCode) {
                this.apiClient.sendTestCodeValidationRequest(Utils.getDigaTestCode(code), appConfig.testInsurance)
            } else {
                // Validate code
                validateCode(code)

                this.apiClient.validateDigaCode(code)
            }
        } catch (e: Exception) {
            logger.error("Validation errors", e)

            throw ValidationException(e.message?.let { e.message } ?: "An unknown exception occurred")
        }

        // If response has errors unwrap them
        if (response.isHasError()) {
            val errors: String = unwrapErrors(response.getErrors()).joinToString(separator = ", ")
            logger.error("Validation errors", errors)

            throw ValidationException(errors)
        }

        return ValidationResponse(
            response.validatedDigaCode,
            response.validatedDigaveid,
            response.dayOfServiceProvision,
            String(response.rawXmlResponseBody)
        )
    }

    @Throws(BillingException::class)
    fun bill(billingRequest: BillingRequest): BillingResponse {
        val isTestCode = Utils.isTestCode(billingRequest.code)

        val invoice = DigaInvoice.builder()
            .invoiceId(billingRequest.invoiceNumber)
            .validatedDigaCode(billingRequest.code)
            .digavEid(billingRequest.digavId)
            .build()

        val response: DigaInvoiceResponse = try {
            if (isTestCode && appConfig.disableTestcodes) {
                throw CodeValidationException("Testcodes are not allowed")
            }
            if (isTestCode) {
                this.apiClient.sendTestInvoiceRequest(invoice, appConfig.testInsurance)
            } else {
                // Validate code
                validateCode(billingRequest.code)

                this.apiClient.invoiceDiga(invoice)
            }
        } catch (e: Exception) {
            logger.error("Billing errors", e)

            throw BillingException(e.message?.let { e.message } ?: "An unknown exception occurred")
        }

        // If response has errors unwrap them
        if (response.isHasError()) {
            val errors: String = unwrapErrors(response.getErrors()).joinToString(separator = ", ")
            logger.error("Billing errors", errors)

            throw BillingException(unwrapErrors(response.getErrors()).joinToString(separator = ", "))
        }

        return BillingResponse(
            billingRequest.code,
            response.isRequiresManualAction,
            response.invoiceMethod,
            response.generatedInvoice
        )
    }

    private fun unwrapErrors(responseErrors: List<DigaApiResponseError>): List<String> {
        val errors: MutableList<String> = ArrayList()
        for (error: DigaApiResponseError in responseErrors) {
            errors.add(error.getError())
        }

        return errors
    }
}
