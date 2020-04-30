package uk.co.suskins.commutestatus.data

data class Status(
    val scheduledTimeOfDeparture: String,
    val estimatedTimeOfDeparture: String,
    val platform: String,
    val delayReason: String,
    val cancelReason: String,
    val isCancelled: Boolean,
    val to: String,
    val from: String
) {
}