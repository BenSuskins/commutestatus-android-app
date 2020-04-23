package uk.co.suskins.commutestatus.models

data class User(
    val firstName: String,
    val lastName: String,
    val email: String,
    val homeStation: String,
    val workStation: String
) {
}