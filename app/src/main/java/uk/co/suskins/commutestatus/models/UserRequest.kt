package uk.co.suskins.commutestatus.models

data class UserRequest(
    val firstName: String,
    val lastName: String,
    val email: String,
    val homeStation: Int,
    val workStation: Int
) {
}