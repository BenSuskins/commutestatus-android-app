package uk.co.suskins.commutestatus.service

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PUT
import uk.co.suskins.commutestatus.data.CommuteStatus
import uk.co.suskins.commutestatus.data.Station
import uk.co.suskins.commutestatus.data.User
import uk.co.suskins.commutestatus.data.UserRequest

interface CommuteStatusService {
    @GET("secure/commutestatus")
    fun getCommuteStatus(@Header("Authorization") accessToken: String?): Call<CommuteStatus>

    @GET("public/stations")
    fun getStations(): Call<List<Station>>

    @GET("secure/user")
    fun getUser(@Header("Authorization") accessToken: String?): Call<User>

    @PUT("secure/user")
    fun putUser(
        @Header("Authorization") accessToken: String?,
        @Body userRequest: UserRequest
    ): Call<User>
}