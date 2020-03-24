package bedbrains.homesweethomeandroidclient

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import bedbrains.homesweethomeandroidclient.rest.APIUtils
import bedbrains.homesweethomeandroidclient.rest.Resp
import bedbrains.shared.datatypes.devices.Device
import bedbrains.shared.datatypes.rules.Rule
import bedbrains.shared.datatypes.rules.RuleValue
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object DataRepository {
    val restClient = APIUtils("http://192.168.178.28:8080").apiService

    val devices: MutableLiveData<MutableList<Device>> = MutableLiveData(mutableListOf())
    val rules: MutableLiveData<MutableList<Rule>> = MutableLiveData(mutableListOf())
    val values: MutableLiveData<MutableList<RuleValue>> = MutableLiveData(mutableListOf())

    init {
        update()
    }

    fun update() {
        updateDevices()
        updateRules()
        updateValues()
    }

    fun updateDevices(): LiveData<Resp> {
        val updated = MutableLiveData(Resp.AWAITING)

        restClient.devices().enqueue(object : Callback<List<Device>> {
            override fun onFailure(call: Call<List<Device>>, t: Throwable) {
                Log.d("TESTING", Log.getStackTraceString(t))
                updated.value = Resp.FAILURE
            }

            override fun onResponse(call: Call<List<Device>>, response: Response<List<Device>>) {
                devices.value = response.body()?.toMutableList() ?: mutableListOf()
                updated.value = when (response.body()) {
                    null -> Resp.FAILURE
                    else -> Resp.SUCCESS
                }
            }

        })

        return updated
    }

    fun updateRules(): LiveData<Resp> {
        val updated = MutableLiveData(Resp.AWAITING)

        restClient.rules().enqueue(object : Callback<List<Rule>> {
            override fun onFailure(call: Call<List<Rule>>, t: Throwable) {
                Log.d("TESTING", Log.getStackTraceString(t))
                updated.value = Resp.FAILURE
            }

            override fun onResponse(call: Call<List<Rule>>, response: Response<List<Rule>>) {
                rules.value = response.body()?.toMutableList() ?: mutableListOf()
                updated.value = when (response.body()) {
                    null -> Resp.FAILURE
                    else -> Resp.SUCCESS
                }
            }

        })

        return updated
    }

    fun updateValues(): LiveData<Resp> {
        val updated = MutableLiveData(Resp.AWAITING)

        restClient.values().enqueue(object : Callback<List<RuleValue>> {
            override fun onFailure(call: Call<List<RuleValue>>, t: Throwable) {
                Log.d("TESTING", Log.getStackTraceString(t))
                updated.value = Resp.FAILURE
            }

            override fun onResponse(call: Call<List<RuleValue>>, response: Response<List<RuleValue>>) {
                values.value = response.body()?.toMutableList() ?: mutableListOf()
                updated.value = when (response.body()) {
                    null -> Resp.FAILURE
                    else -> Resp.SUCCESS
                }
            }

        })

        return updated
    }
}