package dev.gtuk.diga

import dev.gtuk.diga.exceptions.BillingException
import dev.gtuk.diga.exceptions.TestCodesDisabledException
import dev.gtuk.diga.exceptions.ValidationException
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class AppExceptionHandler {

    @ExceptionHandler(ValidationException::class)
    fun handleValidationException(e: ValidationException, response: HttpServletResponse) {
        response.sendError(HttpStatus.BAD_REQUEST.value())
    }

    @ExceptionHandler(BillingException::class)
    fun handleBillingException(e: BillingException, response: HttpServletResponse) {
        response.sendError(HttpStatus.BAD_REQUEST.value())
    }

    @ExceptionHandler(TestCodesDisabledException::class)
    fun handleCodeValidationException(e: TestCodesDisabledException, response: HttpServletResponse) {
        response.sendError(HttpStatus.FORBIDDEN.value())
    }
}
