package uk.co.suskins.commutestatus.service

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import uk.co.suskins.commutestatus.models.CommuteStatus

class CommuteStatusService {
    private val statusList = mutableListOf<CommuteStatus>()
    private val statuses = MutableLiveData<List<CommuteStatus>>()

    init {
        statuses.value = statusList
    }

    fun addQuote(statuses: CommuteStatus) {
//        statusList.add(statuses)
//        statuses.value = statusList
    }

    fun getStatuses(): LiveData<List<CommuteStatus>> {
        return statuses
    }
}