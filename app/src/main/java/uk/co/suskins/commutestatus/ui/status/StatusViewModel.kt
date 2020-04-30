package uk.co.suskins.commutestatus.ui.status

import androidx.lifecycle.ViewModel
import uk.co.suskins.commutestatus.service.CommuteStatusService

class StatusViewModel(var commuteStatusService: CommuteStatusService) :
    ViewModel() {
    fun getStatuses() = commuteStatusService.getStatuses()

    fun getNumberOfWorkStatuses() = 0 //todo

    fun getNumberOfHomeStatuses() = 0 //todo
}
