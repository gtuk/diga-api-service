package dev.gtuk.diga

import com.alextherapeutics.diga.model.DigaApiTestCode
import dev.gtuk.diga.exceptions.TestCodeException
import kotlin.jvm.Throws

class Utils {

    companion object {
        fun isTestCode(code: String): Boolean {
            for (digaCode in DigaApiTestCode.values()) {
                if (digaCode.code == code) {
                    return true
                }
            }

            return false
        }

        @Throws(TestCodeException::class)
        fun getDigaTestCode(code: String): DigaApiTestCode {
            for (digaCode in DigaApiTestCode.values()) {
                if (digaCode.code == code) {
                    return digaCode
                }
            }

            throw TestCodeException("$code is not a valid test code")
        }
    }
}
