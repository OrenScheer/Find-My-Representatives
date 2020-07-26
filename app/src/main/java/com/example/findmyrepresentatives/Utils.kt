package com.example.findmyrepresentatives

/**
 * A class designed to add any functionality that may be needed across multiple parts of the app.
 */
class Utils {
    companion object {
        /**
         * Returns whether or not a user-inputted postal code is of a valid format.
         * However, it does not check whether the postal code is actually a real Canadian postal code.
         * @param postalCode the postal code inputted by the user.
         * @return A Boolean value specifying whether the postal code is of valid format.
         */
        fun isValidPostalCode(postalCode: String): Boolean {
            val pattern = Regex("[a-z][0-9][a-z] ?[0-9][a-z][0-9]", RegexOption.IGNORE_CASE) // Use regex format and allow whitespace
            return pattern matches postalCode
        }
    }
}