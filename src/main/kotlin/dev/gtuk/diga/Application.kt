package dev.gtuk.diga

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication(exclude = [WebMvcAutoConfiguration::class, ErrorMvcAutoConfiguration::class])
@EnableConfigurationProperties(AppConfig::class)
class Application

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}
