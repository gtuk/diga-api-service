package dev.gtuk.diga

import dev.gtuk.diga.dtos.BillingRequest
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
class AppController(val digaService: DigaService) {

    @GetMapping("/validate/{code}")
    fun verify(@PathVariable code: String): ResponseEntity<Any> {
        return ResponseEntity.status(HttpStatus.OK).body(digaService.verify(code))
    }

    @PostMapping("/bill")
    fun billing(@Valid @RequestBody billingRequest: BillingRequest): ResponseEntity<Any> {
        return ResponseEntity.status(HttpStatus.OK).body(digaService.bill(billingRequest))
    }
}
