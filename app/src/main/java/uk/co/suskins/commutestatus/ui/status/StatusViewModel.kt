package uk.co.suskins.commutestatus.ui.status

import androidx.lifecycle.ViewModel
import uk.co.suskins.commutestatus.models.CommuteStatusService

class StatusViewModel(private val commuteStatusService: CommuteStatusService) : ViewModel() {
    fun getStatuses() = commuteStatusService.getStatuses()
}
