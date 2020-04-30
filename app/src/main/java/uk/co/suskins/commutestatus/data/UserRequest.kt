package uk.co.suskins.commutestatus.data

data class UserRequest(
    val firstName: String,
    val lastName: String,
    val password: String?,
    val email: String,
    val homeStation: Int,
    val workStation: Int
) {
}