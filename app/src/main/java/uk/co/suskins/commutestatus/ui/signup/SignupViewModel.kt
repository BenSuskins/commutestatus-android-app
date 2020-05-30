package uk.co.suskins.commutestatus.ui.signup

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import uk.co.suskins.commutestatus.data.StationResponse
import uk.co.suskins.commutestatus.data.User
import uk.co.suskins.commutestatus.data.UserRequest
import uk.co.suskins.commutestatus.service.CommuteStatusService
import uk.co.suskins.commutestatus.ui.status.ERRORED
import uk.co.suskins.commutestatus.ui.status.LOADING
import uk.co.suskins.commutestatus.ui.status.SUCCESS

class SignupViewModel : ViewModel() {
    private val TAG = "SettingsViewModel"
    internal val user: MutableLiveData<User> by lazy {
        MutableLiveData<User>()
    }
    internal val stations: MutableLiveData<StationResponse> by lazy {
        MutableLiveData<StationResponse>()
    }
    val userStatus: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    val stationStatus: MutableLiveData<String> by lazy {
        MutableLiveData<String>(LOADING)
    }

    fun getStations() {
        //Initialise values
        stationStatus.value = LOADING

        val retrofit = Retrofit.Builder()
            .baseUrl("https://localhost:8080/api/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service: CommuteStatusService = retrofit.create(CommuteStatusService::class.java)
        service.getStations().enqueue(
            (object : Callback<StationResponse> {
                override fun onFailure(call: Call<StationResponse>, t: Throwable) {
                    stationStatus.value = ERRORED
                    Log.e(TAG, "Error getting Stations")
                    Log.getStackTraceString(t)
                }

                override fun onResponse(
                    call: Call<StationResponse>,
                    response: Response<StationResponse>
                ) {
                    if (response.isSuccessful) {
                        //If successful update values
                        stations.postValue(response.body())
                        stationStatus.postValue("")
                    } else {
                        //If errored set state and log
                        stationStatus.value = ERRORED
                        Log.e(TAG, "Error getting Stations")
                        Log.e(TAG, "Code: ${response.code()}")
                        Log.e(TAG, "Message: ${response.message()}")
                    }
                }
            })
        )
    }

    fun postUser(userRequest: UserRequest) {
        userStatus.value = LOADING

        val retrofit = Retrofit.Builder()
            .baseUrl("https://localhost:8080/api/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service: CommuteStatusService = retrofit.create(CommuteStatusService::class.java)
        service.postUser(userRequest).enqueue(
            (object : Callback<Void> {
                override fun onFailure(call: Call<Void>, t: Throwable) {
                    stationStatus.value = ERRORED
                    Log.e(TAG, "Error posting User")
                    Log.getStackTraceString(t)
                }

                override fun onResponse(
                    call: Call<Void>,
                    response: Response<Void>
                ) {
                    if (!response.isSuccessful) {
                        //If errored set state and log
                        userStatus.value = ERRORED
                        Log.e(TAG, "Error posting User")
                        Log.e(TAG, "Code: ${response.code()}")
                        Log.e(TAG, "Message: ${response.message()}")
                    }else{
                        userStatus.value = SUCCESS
                    }
                }
            })
        )
    }
}
