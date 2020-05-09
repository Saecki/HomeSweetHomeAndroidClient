package bedbrains.platform

import bedbrains.homesweethomeandroidclient.DataRepository
import bedbrains.shared.datatypes.devices.Device
import bedbrains.shared.datatypes.rules.Rule
import bedbrains.shared.datatypes.rules.RuleValue

object DataProvider {
    val devices: List<Device>
        get() = DataRepository.devices.value ?: listOf()

    val rules: List<Rule>
        get() = DataRepository.rules.value ?: listOf()

    val values: List<RuleValue>
        get() = DataRepository.values.value ?: listOf()

    fun updateDevice(device: Device) = DataRepository.updateDevice(device)

    fun upsertRule(rule: Rule) = DataRepository.upsertRule(rule)

    fun upsertValue(value: RuleValue) = DataRepository.upsertValue(value)
}