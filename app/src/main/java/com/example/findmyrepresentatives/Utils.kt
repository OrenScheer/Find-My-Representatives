package com.example.findmyrepresentatives

class Utils {
    companion object {
        fun isValidPostalCode(postalCode: String): Boolean {
            val pattern = Regex("[a-z][0-9][a-z] ?[0-9][a-z][0-9]", RegexOption.IGNORE_CASE)
            return pattern matches postalCode
        }
    }
}