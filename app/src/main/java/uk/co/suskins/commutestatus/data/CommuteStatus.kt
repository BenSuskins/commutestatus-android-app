package uk.co.suskins.commutestatus.data

data class CommuteStatus(
    val toWork: MutableList<Status>,
    val toHome: MutableList<Status>
) {
}