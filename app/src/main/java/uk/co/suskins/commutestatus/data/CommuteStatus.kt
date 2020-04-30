package uk.co.suskins.commutestatus.data

data class CommuteStatus(
    val toWork: Collection<Status>,
    val toHome: Collection<Status>
) {
}