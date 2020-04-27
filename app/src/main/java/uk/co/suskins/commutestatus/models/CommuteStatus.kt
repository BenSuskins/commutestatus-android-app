package uk.co.suskins.commutestatus.models

data class CommuteStatus(
    val toWork: Collection<Status>,
    val toHome: Collection<Status>
) {
}