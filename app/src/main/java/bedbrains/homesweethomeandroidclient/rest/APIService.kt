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
    fun putDevice(@Body device: Device): Call<Unit>

    @JvmSuppressWildcards
    @PUT("/v1/devices")
    fun putDevices(@Body devices: List<Device>): Call<Unit>

    @GET("/v1/rules")
    fun rules(): Call<List<Rule>>

    @GET("/v1/rules/{uid}")
    fun rule(@Path("uid") uid: String): Call<Rule>

    @PUT("/v1/rules/rule")
    fun putRule(@Body rule: Rule): Call<Unit>

    //TODO remove JvmSuppressWildcards when https://github.com/spinnaker/keel/pull/1173 is fixed
    @JvmSuppressWildcards
    @PUT("/v1/rules")
    fun putRules(@Body rules: List<Rule>): Call<Unit>

    @POST("/v1/rules/rule")
    fun postRule(@Body rule: Rule): Call<Unit>

    @DELETE("/v1/rules/{uid}")
    fun deleteRule(@Path("uid") uid: String): Call<Unit>

    //TODO remove JvmSuppressWildcards when https://github.com/spinnaker/keel/pull/1173 is fixed
    @JvmSuppressWildcards
    //TODO replace with @Delete("path") when https://github.com/square/retrofit/issues/458 is fixed
    @HTTP(method = "DELETE", path = "/v1/rules", hasBody = true)
    fun deleteRules(@Body uids: List<String>): Call<Unit>

    @GET("/v1/values")
    fun values(): Call<List<RuleValue>>

    @GET("/v1/values/{uid}")
    fun value(@Path("uid") uid: String): Call<RuleValue>

    @PUT("/v1/values/value")
    fun putValue(@Body value: RuleValue): Call<Unit>

    @JvmSuppressWildcards
    @PUT("/v1/values")
    fun putValues(@Body values: List<RuleValue>): Call<Unit>

    @POST("/v1/values/value")
    fun postValue(@Body value: RuleValue): Call<Unit>

    @DELETE("/v1/values/{uid}")
    fun deleteValue(@Path("uid") uid: String): Call<Unit>

    //TODO remove JvmSuppressWildcards when https://github.com/spinnaker/keel/pull/1173 is fixed
    @JvmSuppressWildcards
    //TODO replace with @Delete("path") when https://github.com/square/retrofit/issues/458 is fixed
    @HTTP(method = "DELETE", path = "/v1/values", hasBody = true)
    fun deleteValues(@Body uids: List<String>): Call<Unit>

}