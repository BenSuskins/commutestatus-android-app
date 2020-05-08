package uk.co.suskins.commutestatus.data

data class UserRequest(
    val email: String,
    val homeStation: Int,
    val workStation: Int
) {
}