package com.example.findmyrepresentatives

import com.google.common.truth.Truth.assertThat
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class MainActivityTest {
    @Test
    fun postalCode_Correct_ReturnsTrue() {
        assertThat(Utils.isValidPostalCode("K2P1S3")).isTrue()
    }
}