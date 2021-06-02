package dev.gtuk.diga

import dev.gtuk.diga.exceptions.BillingException
import dev.gtuk.diga.exceptions.CodeValidationException
import dev.gtuk.diga.exceptions.TestCodeException
import dev.gtuk.diga.exceptions.ValidationException
import javax.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class AppExceptionHandler {

    private val logger = LoggerFactory.getLogger(javaClass)

    @ExceptionHandler(ValidationException::class)
    fun handleValidationException(e: ValidationException, response: HttpServletResponse) {
        logger.error("Error validating code", e)

        response.sendError(HttpStatus.BAD_REQUEST.value())
    }

    @ExceptionHandler(CodeValidationException::class)
    fun handleCodeValidationException(e: CodeValidationException, response: HttpServletResponse) {
        logger.error("Error validating code", e)

        response.sendError(HttpStatus.BAD_REQUEST.value())
    }

    @ExceptionHandler(BillingException::class)
    fun handleBillingException(e: BillingException, response: HttpServletResponse) {
        logger.error("Error billing", e)

        response.sendError(HttpStatus.BAD_REQUEST.value())
    }

    @ExceptionHandler(TestCodeException::class)
    fun handleTestCodeException(e: TestCodeException, response: HttpServletResponse) {
        logger.error("Error while getting test code", e)

        response.sendError(HttpStatus.BAD_REQUEST.value())
    }
}
