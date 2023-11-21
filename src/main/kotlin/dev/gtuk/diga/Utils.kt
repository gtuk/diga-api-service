package dev.gtuk.diga

import com.alextherapeutics.diga.model.DigaApiTestCode
import dev.gtuk.diga.exceptions.CodeValidationException

class Utils {

    companion object {
        fun isTestCode(code: String): Boolean {
            return code.startsWith("77")
        }

        @Throws(CodeValidationException::class)
        fun getDigaTestCode(code: String): DigaApiTestCode {
            for (digaCode in DigaApiTestCode.values()) {
                if (digaCode.code == code) {
                    return digaCode
                }
            }

            throw CodeValidationException("$code is not a valid test code")
        }
    }
}
