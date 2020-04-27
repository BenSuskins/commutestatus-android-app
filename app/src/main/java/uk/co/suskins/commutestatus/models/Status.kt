package uk.co.suskins.commutestatus.models

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