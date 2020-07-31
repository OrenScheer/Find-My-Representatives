package com.example.findmyrepresentatives

import java.util.*

/**
 * A RepresentDataSet is the return of a call to the API.
 * For the Moshi deserializer, there is no interest in anything other than
 * the list of representatives, so all the other fields except for postal code are
 * set to null.
 * @author Oren Scheer
 */
data class RepresentDataSet (
    val objects: MutableList<Representative>?, // In the case of using device location
    val representatives_centroid: MutableList<Representative>?, // In the case of using postal code
    val representatives_concordance: MutableList<Representative>?, // In the case of using postal code
    val city: Any? = null,
    val boundaries_concordance: Any? = null,
    val province: Any? = null,
    val centroid: Any? = null,
    val boundaries_centroid: Any? = null,
    val code: String? // Keep the postal code so it can be part of query history if implemented
)
