package dev.gtuk.diga

import dev.gtuk.diga.dtos.ValidationErrorResponse
import dev.gtuk.diga.exceptions.BillingException
import dev.gtuk.diga.exceptions.DigaCodeValidationException
import dev.gtuk.diga.exceptions.TestCodesDisabledException
import dev.gtuk.diga.exceptions.ValidationException
import javax.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
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

    @ExceptionHandler(DigaCodeValidationException::class)
    fun handleDigaAPICodeValidationException(e: DigaCodeValidationException, response: HttpServletResponse):
        ResponseEntity<Any> {
        return ResponseEntity.badRequest()
            .body(ValidationErrorResponse(HttpStatus.BAD_REQUEST.value(), e.errorCode,
            e.errorText, "/validate/${e.code}"))
    }
}
