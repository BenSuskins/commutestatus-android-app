package uk.co.suskins.commutestatus.ui.status

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import uk.co.suskins.commutestatus.data.CommuteStatus

class StatusViewModel : ViewModel() {
    private val status: MutableLiveData<String> by lazy {
        MutableLiveData<String>("loading")
    }

    internal val commuteStatus: MutableLiveData<CommuteStatus> by lazy {
        MutableLiveData<CommuteStatus>()
    }

    fun getCommuteStatus(idToken: String?): MutableLiveData<CommuteStatus> {
        status.value = "loading"
        //Todo call api

        //Todo update live data with response
//        status.value = ""
        return commuteStatus
    }

    fun getNumberOfWorkStatuses() = commuteStatus.value?.toWork?.size

    fun getNumberOfHomeStatuses() = commuteStatus.value?.toHome?.size

    fun isLoading(): MutableLiveData<Boolean> {
        val isLoading: MutableLiveData<Boolean> by lazy {
            MutableLiveData<Boolean>()
        }

        if (status.value?.equals("loading")!!)
            isLoading.value = true
        else
            isLoading.value = false

        return isLoading
    }
}
