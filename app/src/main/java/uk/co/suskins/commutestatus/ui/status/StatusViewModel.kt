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
import uk.co.suskins.commutestatus.data.User
import uk.co.suskins.commutestatus.service.CommuteStatusService


class StatusViewModel : ViewModel() {
    private val TAG = "StatusViewModel"
    var workIndex = 0
    var homeIndex = 0
    val commuteStatusStatus: MutableLiveData<String> by lazy {
        MutableLiveData<String>(LOADING)
    }
    val userStatus: MutableLiveData<String> by lazy {
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
    internal val user: MutableLiveData<User> by lazy {
        MutableLiveData<User>()
    }

    fun getData(accessToken: String?) {
        //Initialise values
        commuteStatusStatus.value = LOADING
        workIndex = 0
        homeIndex = 0

        val retrofit = Retrofit.Builder()
            .baseUrl("https://localhost:8080/api/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        //Call API with Access Token
        val service: CommuteStatusService = retrofit.create(CommuteStatusService::class.java)
        getCommuteStatus(service, accessToken)
        getUser(service, accessToken)
    }

    private fun getCommuteStatus(
        service: CommuteStatusService,
        accessToken: String?
    ) {
        service.getCommuteStatus("Bearer $accessToken").enqueue(
            (object : Callback<CommuteStatus> {
                override fun onFailure(call: Call<CommuteStatus>, t: Throwable) {
                    commuteStatusStatus.value = ERRORED
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
                        if (!commuteStatus.value!!.toHome.isEmpty()) {
                            toHome.postValue(commuteStatus.value?.toHome?.elementAt(homeIndex))
                        } else {
                            toHome.postValue(null)
                        }
                        if (!commuteStatus.value!!.toWork.isEmpty()) {
                            toWork.postValue(commuteStatus.value?.toWork?.elementAt(workIndex))
                        } else {
                            toWork.postValue(null)
                        }
                        commuteStatusStatus.postValue("")
                    } else {
                        //If errored set state and log
                        commuteStatusStatus.value = ERRORED
                        Log.e(TAG, "Error getting Commute Status")
                        Log.e(TAG, "Code: ${response.code()}")
                        Log.e(TAG, "Message: ${response.message()}")
                    }
                }
            })
        )
    }

    private fun getUser(
        service: CommuteStatusService,
        accessToken: String?
    ) {
        service.getUser("Bearer $accessToken").enqueue(
            (object : Callback<User> {
                override fun onFailure(call: Call<User>, t: Throwable) {
                    userStatus.value = ERRORED
                    Log.e(TAG, "Error getting User")
                    Log.getStackTraceString(t)
                }

                override fun onResponse(
                    call: Call<User>,
                    response: Response<User>
                ) {
                    if (response.isSuccessful) {
                        //If successful update values
                        user.postValue(response.body())
                        userStatus.postValue("")
                    } else {
                        //If errored set state and log
                        userStatus.value = ERRORED
                        Log.e(TAG, "Error getting User")
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
        if (!commuteStatus.value!!.toHome.isEmpty()) {
            homeIndex++
            if (homeIndex >= getNumberOfHomeStatuses()!!) {
                homeIndex = 0
            }
            toHome.postValue(commuteStatus.value?.toHome?.elementAt(homeIndex))
        }

        if (!commuteStatus.value!!.toWork.isEmpty()) {
            workIndex++
            if (workIndex >= getNumberOfWorkStatuses()!!) {
                workIndex = 0
            }
            toWork.postValue(commuteStatus.value?.toWork?.elementAt(workIndex))
        }
    }
}
