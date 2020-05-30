package uk.co.suskins.commutestatus.service

import retrofit2.Call
import retrofit2.http.*
import uk.co.suskins.commutestatus.data.CommuteStatus
import uk.co.suskins.commutestatus.data.StationResponse
import uk.co.suskins.commutestatus.data.User
import uk.co.suskins.commutestatus.data.UserRequest

interface CommuteStatusService {
    @GET("secure/commutestatus")
    fun getCommuteStatus(@Header("Authorization") accessToken: String?): Call<CommuteStatus>

    @GET("public/stations")
    fun getStations(): Call<StationResponse>

    @GET("secure/user")
    fun getUser(@Header("Authorization") accessToken: String?): Call<User>

    @PUT("secure/user")
    fun putUser(
        @Header("Authorization") accessToken: String?,
        @Body userRequest: UserRequest
    ): Call<User>

    @POST("public/user")
    fun postUser(@Body userRequest: UserRequest): Call<User>
}