package dev.gtuk.diga

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableConfigurationProperties(AppConfig::class)
class Application

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}
