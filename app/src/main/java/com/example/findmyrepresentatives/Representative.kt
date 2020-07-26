package com.example.findmyrepresentatives

/**
 * A Representative object contains all the relevant information about a single
 * representative, as returned by the Represent API.
 */
class Representative (
    val photo_url: String,
    val first_name: String,
    val last_name: String,
    val district_name: String,
    val representative_set_name: String,
    val elected_office: String,
    val email: String
)