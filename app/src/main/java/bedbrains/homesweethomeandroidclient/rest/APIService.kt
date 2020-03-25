package bedbrains.homesweethomeandroidclient.rest

import bedbrains.shared.datatypes.devices.Device
import bedbrains.shared.datatypes.rules.Rule
import bedbrains.shared.datatypes.rules.RuleValue
import retrofit2.Call
import retrofit2.http.*

interface APIService {

    @GET("/v1/devices")
    fun devices(): Call<List<Device>>

    @GET("/v1/device")
    fun device(@Query("uid") uid: String): Call<Device>

    @POST("/v1/device")
    fun postDevice(@Body device: Device): Call<Unit>

    @DELETE("/v1/device")
    fun deleteDevice(@Query("uid") uid: String): Call<Unit>

    @GET("/v1/rules")
    fun rules(): Call<List<Rule>>

    @GET("/v1/rule")
    fun rule(@Query("uid") uid: String): Call<Rule>

    @POST("/v1/rule")
    fun postRule(@Body rule: Rule): Call<Unit>

    @DELETE("/v1/rule")
    fun deleteRule(@Query("uid") uid: String): Call<Unit>

    @GET("/v1/values")
    fun values(): Call<List<RuleValue>>

    @GET("/v1/value")
    fun value(@Query("uid") uid: String): Call<RuleValue>

    @POST("/v1/value")
    fun postValue(@Body value: RuleValue): Call<Unit>

    @DELETE("/v1/value")
    fun deleteValue(@Query("uid") uid: String): Call<Unit>
}