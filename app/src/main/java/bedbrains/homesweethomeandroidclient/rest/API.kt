package bedbrains.homesweethomeandroidclient.rest

import bedbrains.shared.datatypes.devices.Device
import bedbrains.shared.datatypes.rules.Rule
import bedbrains.shared.datatypes.rules.RuleValue
import retrofit2.Call
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface APIService {

    @GET("/v1/devices")
    fun devices(): Call<List<Device>>

    @GET("/v1/device")
    fun device(@Query("uid") uid: String): Call<Device>

    @POST("/v1/device")
    fun postDevice(device: Device)

    @DELETE("/v1/device")
    fun deleteDevice(@Query("uid") uid: String)

    @GET("/v1/rules")
    fun rules(): Call<List<Rule>>

    @GET("/v1/rule")
    fun rule(@Query("uid") uid: String): Call<Rule>

    @POST("/v1/rule")
    fun postRule(rule: Rule)

    @DELETE("/v1/rule")
    fun deleteRule(@Query("uid") uid: String)

    @GET("/v1/values")
    fun values(): Call<List<RuleValue>>

    @GET("/v1/value")
    fun value(@Query("uid") uid: String): Call<RuleValue>

    @POST("/v1/value")
    fun postValue(value: RuleValue)

    @DELETE("/v1/value")
    fun deleteValue(@Query("uid") uid: String)
}

class APIUtils(val baseUrl: String) {
    val apiService: APIService
        get() = Controller.buildClient(baseUrl).create(APIService::class.java)
}