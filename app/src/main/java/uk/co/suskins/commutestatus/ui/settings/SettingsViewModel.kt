package uk.co.suskins.commutestatus.ui.settings

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

class SettingsViewModel : ViewModel() {
    private val TAG = "SettingsViewModel"
    internal val user: MutableLiveData<User> by lazy {
        MutableLiveData<User>()
    }
    internal val stations: MutableLiveData<StationResponse> by lazy {
        MutableLiveData<StationResponse>()
    }
    val userStatus: MutableLiveData<String> by lazy {
        MutableLiveData<String>(LOADING)
    }
    val stationStatus: MutableLiveData<String> by lazy {
        MutableLiveData<String>(LOADING)
    }

    fun getData(accessToken: String?) {
        //Initialise values
        userStatus.value = LOADING
        stationStatus.value = LOADING

        val retrofit = Retrofit.Builder()
            .baseUrl("https://localhost:8080/api/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service: CommuteStatusService = retrofit.create(CommuteStatusService::class.java)

        //Call API with Access Token
        getUser(service, accessToken)
        getStations(service)
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

    private fun getStations(service: CommuteStatusService) {
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

    fun putUser(userRequest: UserRequest, idToken: String?) {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://localhost:8080/api/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service: CommuteStatusService = retrofit.create(CommuteStatusService::class.java)
        service.putUser("Bearer $idToken", userRequest).enqueue(
            (object : Callback<User> {
                override fun onFailure(call: Call<User>, t: Throwable) {
                    stationStatus.value = ERRORED
                    Log.e(TAG, "Error putting User")
                    Log.getStackTraceString(t)
                }

                override fun onResponse(
                    call: Call<User>,
                    response: Response<User>
                ) {
                    if (response.isSuccessful) {
                        //If successful update values
                        user.postValue(response.body())
                    } else {
                        //If errored set state and log
                        userStatus.value = ERRORED
                        Log.e(TAG, "Error putting User")
                        Log.e(TAG, "Code: ${response.code()}")
                        Log.e(TAG, "Message: ${response.message()}")
                    }
                }
            })
        )
    }
}
