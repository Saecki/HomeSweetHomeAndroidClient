package saecki.homesweethomeandroidclient.datatypes.devices

import saecki.homesweethomeandroidclient.datatypes.Temperature

class Heating(
    id: String,
    name: String,
    room: String,
    var actualTemp: Temperature,
    var targetTemp: Temperature
) : Device(id, type, name, room) {

    var extended = false

    companion object {
        const val type = 1
    }
}