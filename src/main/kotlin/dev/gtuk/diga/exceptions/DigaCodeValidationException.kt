package dev.gtuk.diga.exceptions

class DigaCodeValidationException(var errorCode: String, var errorText: String, var code: String) : Exception()
