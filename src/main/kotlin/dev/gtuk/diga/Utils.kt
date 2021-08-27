package dev.gtuk.diga

import com.alextherapeutics.diga.model.DigaApiTestCode
import dev.gtuk.diga.exceptions.DigaCodeValidationException
import kotlin.jvm.Throws

class Utils {

    companion object {
        fun isTestCode(code: String): Boolean {
            return code.startsWith("77")
        }

        @Throws(DigaCodeValidationException::class)
        fun getDigaTestCode(code: String): DigaApiTestCode {
            for (digaCode in DigaApiTestCode.values()) {
                if (digaCode.code == code) {
                    return digaCode
                }
            }
            throw DigaCodeValidationException("INVALID_TEST_CODE", "$code is not a valid test code", code)
        }
    }
}
