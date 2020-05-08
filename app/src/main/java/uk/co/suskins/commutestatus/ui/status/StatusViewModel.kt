package uk.co.suskins.commutestatus.ui.status

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import uk.co.suskins.commutestatus.data.CommuteStatus
import uk.co.suskins.commutestatus.data.Status
import uk.co.suskins.commutestatus.service.CommuteStatusService


class StatusViewModel : ViewModel() {
    private val TAG = "StatusViewModel"
    var workIndex = 0
    var homeIndex = 0
    val status: MutableLiveData<String> by lazy {
        MutableLiveData<String>(LOADING)
    }
    internal val commuteStatus: MutableLiveData<CommuteStatus> by lazy {
        MutableLiveData<CommuteStatus>()
    }
    internal val toWork: MutableLiveData<Status> by lazy {
        MutableLiveData<Status>()
    }
    internal val toHome: MutableLiveData<Status> by lazy {
        MutableLiveData<Status>()
    }

    fun getCommuteStatus(accessToken: String?) {
        //Initialise values
        status.value = LOADING
        workIndex = 0
        homeIndex = 0

        val retrofit = Retrofit.Builder()
            .baseUrl("https://commutestatusapi.suskins.co.uk/api/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        //Call API with Access Token
        val service: CommuteStatusService = retrofit.create(CommuteStatusService::class.java)
        service.getCommuteStatus("Bearer $accessToken").enqueue(
            (object : Callback<CommuteStatus> {
                override fun onFailure(call: Call<CommuteStatus>, t: Throwable) {
                    status.value = ERRORED
                    Log.e(TAG, "Error getting Commute Status")
                    Log.getStackTraceString(t)
                }

                override fun onResponse(
                    call: Call<CommuteStatus>,
                    response: Response<CommuteStatus>
                ) {
                    if (response.isSuccessful) {
                        //If successful update values
                        commuteStatus.value = response.body()
                        toWork.postValue(commuteStatus.value?.toWork?.elementAt(workIndex)!!)
                        toHome.postValue(commuteStatus.value?.toHome?.elementAt(homeIndex)!!)
                        status.postValue("")
                    } else {
                        //If errored set state and log
                        status.value = ERRORED
                        Log.e(TAG, "Error getting Commute Status")
                        Log.e(TAG, "Code: ${response.code()}")
                        Log.e(TAG, "Message: ${response.message()}")
                    }
                }
            })
        )
    }

    fun getToWorkStatus(): MutableLiveData<Status> {
        return toWork
    }

    fun getToHomeStatus(): MutableLiveData<Status> {
        return toHome
    }

    fun getNumberOfWorkStatuses(): Int? {
        if (commuteStatus.value == null) {
            return 0
        } else {
            return commuteStatus.value?.toWork?.size
        }
    }

    fun getNumberOfHomeStatuses(): Int? {
        if (commuteStatus.value == null) {
            return 0
        } else {
            return commuteStatus.value?.toHome?.size
        }
    }

    fun incrementIndex() {
        homeIndex++
        if (homeIndex >= getNumberOfHomeStatuses()!!) {
            homeIndex = 0
        }
        toHome.postValue(commuteStatus.value?.toHome?.elementAt(homeIndex))

        workIndex++
        if (workIndex >= getNumberOfWorkStatuses()!!) {
            workIndex = 0
        }
        toWork.postValue(commuteStatus.value?.toWork?.elementAt(workIndex))
    }
}
