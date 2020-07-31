package com.example.findmyrepresentatives

/**
 * A Representative object contains all the relevant information about a single
 * representative, as returned by the Represent API.
 * @author Oren Scheer
 */
class Representative (
    val photo_url: String,
    val party_name: String,
    val name: String,
    val district_name: String,
    val representative_set_name: String,
    val elected_office: String,
    val email: String
)