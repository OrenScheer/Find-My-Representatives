package com.example.findmyrepresentatives

import com.google.common.truth.Truth.assertThat
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class UtilsTest {
    @Test
    fun isValidPostalCode_CorrectlyFormatted_ReturnsTrue() {
        assertThat(Utils.isValidPostalCode("K2P1S3")).isTrue()
    }

    @Test
    fun isValidPostalCode_CorrectlyFormattedWithSpace_ReturnsTrue() {
        assertThat(Utils.isValidPostalCode("K2P 1S3")).isTrue()
    }

    @Test
    fun isValidPostalCode_CorrectlyFormattedLowerCase_ReturnsTrue() {
        assertThat(Utils.isValidPostalCode("k2p1s3")).isTrue()
    }

    @Test
    fun isValidPostalCode_EmptyString_ReturnsFalse() {
        assertThat(Utils.isValidPostalCode("")).isFalse()
    }

    @Test
    fun isValidPostalCode_IncorrectlyFormattedOnlyLetters_ReturnsFalse() {
        assertThat(Utils.isValidPostalCode("KSL IWO")).isFalse()
    }

    @Test
    fun isValidPostalCode_IncorrectlyFormattedOnlyNumbers_ReturnsFalse() {
        assertThat(Utils.isValidPostalCode("261145")).isFalse()
    }

    @Test
    fun isValidPostalCode_IncorrectlyFormattedWrongOrder_ReturnsFalse() {
        assertThat(Utils.isValidPostalCode("A30 LB2")).isFalse()
    }
}