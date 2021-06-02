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
import dev.gtuk.diga.exceptions.TestCodeException
import dev.gtuk.diga.exceptions.ValidationException
import java.io.FileInputStream
import kotlin.jvm.Throws
import org.springframework.stereotype.Service

@Service
class DigaService(private val appConfig: AppConfig) {
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

    @Throws(ValidationException::class, CodeValidationException::class, TestCodeException::class)
    fun verify(code: String): ValidationResponse {
        val isTestCode = Utils.isTestCode(code)

        // Validate code if it is not a test code
        if (!isTestCode) {
            validateCode(code)
        }

        val response: DigaCodeValidationResponse = if (isTestCode) {
            this.apiClient.sendTestCodeValidationRequest(Utils.getDigaTestCode(code), appConfig.testInsurance)
        } else {
            this.apiClient.validateDigaCode(code)
        }

        // If response has errors unwrap them
        if (response.isHasError()) {
            throw ValidationException(unwrapErrors(response.getErrors()).joinToString(separator = ", "))
        }

        return ValidationResponse(response.validatedDigaCode, response.validatedDigaveid, response.dayOfServiceProvision)
    }

    @Throws(BillingException::class, CodeValidationException::class)
    fun bill(billingRequest: BillingRequest): BillingResponse {
        val isTestCode = Utils.isTestCode(billingRequest.code)

        // Validate code if it is not a test code
        if (!isTestCode) {
            validateCode(billingRequest.code)
        }

        val invoice = DigaInvoice.builder()
            .invoiceId(billingRequest.invoiceNumber)
            .validatedDigaCode(billingRequest.code)
            .digavEid(billingRequest.digavId)
            .build()

        val response: DigaInvoiceResponse = if (isTestCode) {
                this.apiClient.sendTestInvoiceRequest(invoice, appConfig.testInsurance)
        } else {
            this.apiClient.invoiceDiga(invoice)
        }

        // If response has errors unwrap them
        if (response.isHasError()) {
            throw BillingException(unwrapErrors(response.getErrors()).joinToString(separator = ", "))
        }

        return BillingResponse(billingRequest.code, response.isRequiresManualAction, response.invoiceMethod, response.generatedInvoice)
    }

    private fun unwrapErrors(responseErrors: List<DigaApiResponseError>): List<String> {
        val errors: MutableList<String> = ArrayList()
        for (error: DigaApiResponseError in responseErrors) {
            errors.add(error.getError())
        }

        return errors
    }
}
