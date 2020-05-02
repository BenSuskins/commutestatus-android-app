package uk.co.suskins.commutestatus.ui.status

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import uk.co.suskins.commutestatus.data.CommuteStatus
import uk.co.suskins.commutestatus.service.CommuteStatusService


class StatusViewModel : ViewModel() {
    var workIndex = 0
    var homeIndex = 0
    val status: MutableLiveData<String> by lazy {
        MutableLiveData<String>(LOADING)
    }

    internal val commuteStatus: MutableLiveData<CommuteStatus> by lazy {
        MutableLiveData<CommuteStatus>()
    }

    fun getCommuteStatus(accessToken: String?): MutableLiveData<CommuteStatus> {
        status.value = LOADING

        val retrofit = Retrofit.Builder()
            .baseUrl("https://commutestatusapi.suskins.co.uk/api/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service: CommuteStatusService = retrofit.create(CommuteStatusService::class.java)
        service.getCommuteStatus("Bearer " + accessToken).enqueue(
            (object : Callback<CommuteStatus> {
                override fun onFailure(call: Call<CommuteStatus>, t: Throwable) {
                    status.value = ERRORED
                }

                override fun onResponse(
                    call: Call<CommuteStatus>,
                    response: Response<CommuteStatus>
                ) {
                    if (response.isSuccessful) {
                        commuteStatus.value = response.body()
                        status.value = ""
                    } else {
                        status.value = ERRORED
                    }
                }
            })
        )

        return commuteStatus
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

    fun incrementIndex(){
        homeIndex++
        if (homeIndex >= getNumberOfHomeStatuses()!!) {
            homeIndex = 0
        }

        workIndex++
        if (workIndex >= getNumberOfWorkStatuses()!!) {
            workIndex = 0
        }
    }
}
