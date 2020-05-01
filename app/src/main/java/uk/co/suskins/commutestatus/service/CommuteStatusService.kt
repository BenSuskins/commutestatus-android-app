package uk.co.suskins.commutestatus.service

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import uk.co.suskins.commutestatus.data.CommuteStatus

interface CommuteStatusService {
    @GET("secure/commutestatus")
    fun getCommuteStatus(@Header("Authorization") accessToken: String?): Call<CommuteStatus>
}