package bedbrains.homesweethomeandroidclient

import androidx.lifecycle.*
import bedbrains.homesweethomeandroidclient.rest.Controller
import bedbrains.homesweethomeandroidclient.rest.Resp
import bedbrains.shared.datatypes.devices.Device
import bedbrains.shared.datatypes.rules.Rule
import bedbrains.shared.datatypes.rules.RuleValue
import bedbrains.shared.datatypes.upsert
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object DataRepository {
    val restClient = Controller.buildClient("http://192.168.178.55:8080")

    val devices: MutableLiveData<MutableList<Device>> = MutableLiveData(mutableListOf())
    val rules: MutableLiveData<MutableList<Rule>> = MutableLiveData(mutableListOf())
    val values: MutableLiveData<MutableList<RuleValue>> = MutableLiveData(mutableListOf())

    init {
        fetchUpdates()
    }

    fun fetchUpdates(): LiveData<Resp> {
        return resultOf(fetchDevices(), fetchRules(), fetchValues())
    }

    fun fetchDevices(): LiveData<Resp> {
        val responded = MutableLiveData(Resp.AWAITING)

        restClient.devices().enqueue(object : Callback<List<Device>> {
            override fun onFailure(call: Call<List<Device>>, t: Throwable) {
                responded.value = Resp.FAILURE
            }

            override fun onResponse(call: Call<List<Device>>, response: Response<List<Device>>) {
                devices.value = response.body()?.toMutableList() ?: mutableListOf()
                responded.value = when (response.body()) {
                    null -> Resp.FAILURE
                    else -> Resp.SUCCESS
                }
            }

        })

        return responded
    }

    fun upsertDevice(device: Device) {
        devices.value?.upsert(device) { it.uid == device.uid }
        restClient.postDevice(device).enqueue(object : Callback<Unit> {
            override fun onFailure(call: Call<Unit>, t: Throwable) {}
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {}
        })
    }

    fun fetchRules(): LiveData<Resp> {
        val responded = MutableLiveData(Resp.AWAITING)

        restClient.rules().enqueue(object : Callback<List<Rule>> {
            override fun onFailure(call: Call<List<Rule>>, t: Throwable) {
                responded.value = Resp.FAILURE
            }

            override fun onResponse(call: Call<List<Rule>>, response: Response<List<Rule>>) {
                rules.value = response.body()?.toMutableList() ?: mutableListOf()
                responded.value = when (response.body()) {
                    null -> Resp.FAILURE
                    else -> Resp.SUCCESS
                }
            }

        })

        return responded
    }

    fun upsertRule(rule: Rule) {
        rules.value?.upsert(rule) { it.uid == rule.uid }
        restClient.postRule(rule).enqueue(object : Callback<Unit> {
            override fun onFailure(call: Call<Unit>, t: Throwable) {}
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {}
        })
    }

    fun fetchValues(): LiveData<Resp> {
        val responded = MutableLiveData(Resp.AWAITING)

        restClient.values().enqueue(object : Callback<List<RuleValue>> {
            override fun onFailure(call: Call<List<RuleValue>>, t: Throwable) {
                responded.value = Resp.FAILURE
            }

            override fun onResponse(call: Call<List<RuleValue>>, response: Response<List<RuleValue>>) {
                values.value = response.body()?.toMutableList() ?: mutableListOf()
                responded.value = when (response.body()) {
                    null -> Resp.FAILURE
                    else -> Resp.SUCCESS
                }
            }

        })

        return responded
    }

    fun upsertValue(value: RuleValue) {
        values.value?.upsert(value) { it.uid == value.uid }
        restClient.postValue(value).enqueue(object : Callback<Unit> {
            override fun onFailure(call: Call<Unit>, t: Throwable) {}
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {}
        })
    }

    fun resultOf(vararg responses: LiveData<Resp>): LiveData<Resp> {
        val responded = MutableLiveData(Resp.AWAITING)

        for (r in responses) {
            r.observe(MainActivity.activity, Observer { it ->
                when (it) {
                    Resp.AWAITING -> Unit
                    Resp.FAILURE -> {
                        responded.value = Resp.FAILURE
                        responses.forEach { resp -> resp.removeObservers(MainActivity.activity) }
                    }
                    Resp.SUCCESS -> {
                        if (responses.indexOfFirst { resp -> resp.value != Resp.SUCCESS } == -1) {
                            responded.value = Resp.SUCCESS
                        }
                        r.removeObservers(MainActivity.activity)
                    }
                }
            })
        }

        return responded
    }
}