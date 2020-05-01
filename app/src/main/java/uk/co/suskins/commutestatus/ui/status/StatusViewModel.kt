package uk.co.suskins.commutestatus.ui.status

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import uk.co.suskins.commutestatus.data.CommuteStatus

class StatusViewModel : ViewModel() {
    val status: MutableLiveData<String> by lazy {
        MutableLiveData<String>("loading")
    }

    internal val commuteStatus: MutableLiveData<CommuteStatus> by lazy {
        MutableLiveData<CommuteStatus>()
    }

    fun getCommuteStatus(idToken: String?): MutableLiveData<CommuteStatus> {
        status.value = "loading"
        //Todo call api

        //Todo update live data with response
//        commuteStatus.value = apiResponse
        status.value = ""
        return commuteStatus
    }

    fun getNumberOfWorkStatuses() = commuteStatus.value?.toWork?.size

    fun getNumberOfHomeStatuses() = commuteStatus.value?.toHome?.size
}
