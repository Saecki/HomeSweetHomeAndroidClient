package bedbrains.homesweethomeandroidclient

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import bedbrains.homesweethomeandroidclient.rest.*
import bedbrains.shared.datatypes.devices.Device
import bedbrains.shared.datatypes.rules.Rule
import bedbrains.shared.datatypes.rules.RuleValue
import bedbrains.shared.datatypes.updated
import bedbrains.shared.datatypes.upserted

object DataRepository {
    var restClient: APIService? = buildRestClient()

    val handler = Handler(Looper.getMainLooper())
    var updateRunnable = buildUpdateRunnable(5000)

    val devices: MutableLiveData<List<Device>> = MutableLiveData(listOf())
    val rules: MutableLiveData<List<Rule>> = MutableLiveData(listOf())
    val values: MutableLiveData<List<RuleValue>> = MutableLiveData(listOf())

    init {
        fetchUpdates()
    }

    fun fetchUpdates(): LiveData<Resp> {
        return resultOf(fetchDevices(), fetchRules(), fetchValues())
    }


    fun fetchDevices() = RespCallback<List<Device>>().enqueue(restClient?.devices()) {
        devices.value = it?.toMutableList() ?: mutableListOf()
    }

    fun updateDevice(device: Device): LiveData<Resp> {
        devices.value = devices.value?.updated(device) { it.uid == device.uid }
        return RespCallback<Unit>().enqueue(restClient?.putDevice(device))
    }

    fun updateDevices(list: List<Device>): LiveData<Resp> {
        for (d in list) {
            devices.value = devices.value?.updated(d) { it.uid == d.uid }
        }
        return RespCallback<Unit>().enqueue(restClient?.putDevices(list))
    }

    fun fetchRules() = RespCallback<List<Rule>>().enqueue(restClient?.rules()) {
        rules.value = it?.toMutableList() ?: mutableListOf()
    }

    fun updateRule(rule: Rule): LiveData<Resp> {
        rules.value = rules.value?.updated(rule) { it.uid == rule.uid }
        return RespCallback<Unit>().enqueue(restClient?.putRule(rule))
    }

    fun updateRules(list: List<Rule>): LiveData<Resp> {
        for (r in list) {
            rules.value = rules.value?.updated(r) { it.uid == r.uid }
        }
        return RespCallback<Unit>().enqueue(restClient?.putRules(list))
    }

    fun upsertRule(rule: Rule): LiveData<Resp> {
        rules.value = rules.value?.upserted(rule) { it.uid == rule.uid }
        return RespCallback<Unit>().enqueue(restClient?.postRule(rule))
    }

    fun removeRule(rule: Rule): LiveData<Resp> {
        rules.value = rules.value?.minus(rule)
        return RespCallback<Unit>().enqueue(restClient?.deleteRule(rule.uid))
    }

    fun removeRules(list: List<Rule>): LiveData<Resp> {
        rules.value = rules.value?.minus(list)
        return RespCallback<Unit>().enqueue(restClient?.deleteRules(list.map { it.uid }))
    }


    fun fetchValues() = RespCallback<List<RuleValue>>().enqueue(restClient?.values()) {
        values.value = it?.toMutableList() ?: mutableListOf()
    }

    fun updateValue(value: RuleValue): LiveData<Resp> {
        values.value = values.value?.updated(value) { it.uid == value.uid }
        return RespCallback<Unit>().enqueue(restClient?.putValue(value))
    }

    fun updateValues(list: List<RuleValue>): LiveData<Resp> {
        for (v in list) {
            values.value = values.value?.updated(v) { it.uid == v.uid }
        }
        return RespCallback<Unit>().enqueue(restClient?.putValues(list))
    }

    fun upsertValue(value: RuleValue): LiveData<Resp> {
        values.value = values.value?.upserted(value) { it.uid == value.uid }
        return RespCallback<Unit>().enqueue(restClient?.postValue(value))
    }

    fun removeValue(value: RuleValue): LiveData<Resp> {
        values.value = values.value?.minus(value)
        return RespCallback<Unit>().enqueue(restClient?.deleteValue(value.uid))
    }

    //TODO fun removeRemoveValues(list: List<Rule>): LiveData<Resp>


    fun startAutomaticUpdate(delay: Long) {
        handler.removeCallbacks(updateRunnable)
        updateRunnable = buildUpdateRunnable(delay * 1000)
        handler.post(updateRunnable)
    }

    fun stopAutomaticUpdate() {
        handler.removeCallbacks(updateRunnable)
    }

    fun buildUpdateRunnable(delayMillis: Long) = object : Runnable {
        override fun run() {
            fetchUpdates()
            handler.postDelayed(this, delayMillis)
        }
    }

    fun buildRestClient(host: String? = null, port: String? = null): APIService? {
        val h = host ?: Res.preferences.getString(
            Res.resources.getString(R.string.pref_network_host_key),
            ""
        )

        val p = port ?: Res.preferences.getString(
            Res.resources.getString(R.string.pref_network_port_key),
            ""
        )

        return try {
            Controller.buildClient("http://${h?.trim()}:${p?.trim()}")
        } catch (e: Exception) {
            null
        }
    }
}