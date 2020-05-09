package bedbrains.homesweethomeandroidclient.rest

import bedbrains.shared.datatypes.devices.Device
import bedbrains.shared.datatypes.rules.Rule
import bedbrains.shared.datatypes.rules.RuleValue
import retrofit2.Call
import retrofit2.http.*

interface APIService {

    @GET("/v1/devices")
    fun devices(): Call<List<Device>>

    @GET("/v1/devices/{uid}")
    fun device(@Path("uid") uid: String): Call<Device>

    @PUT("/v1/devices/device")
    fun postDevice(@Body device: Device): Call<Unit>

    @GET("/v1/rules")
    fun rules(): Call<List<Rule>>

    @GET("/v1/rules/{uid}")
    fun rule(@Path("uid") uid: String): Call<Rule>

    @POST("/v1/rules/rule")
    fun postRule(@Body rule: Rule): Call<Unit>

    @DELETE("/v1/rules/{uid}")
    fun deleteRule(@Path("uid") uid: String): Call<Unit>

    @GET("/v1/values")
    fun values(): Call<List<RuleValue>>

    @GET("/v1/values/{uid}")
    fun value(@Path("uid") uid: String): Call<RuleValue>

    @POST("/v1/values/value")
    fun postValue(@Body value: RuleValue): Call<Unit>

    @DELETE("/v1/values/{uid}")
    fun deleteValue(@Path("uid") uid: String): Call<Unit>
}