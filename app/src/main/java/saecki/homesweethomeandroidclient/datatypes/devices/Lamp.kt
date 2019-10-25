package saecki.homesweethomeandroidclient.datatypes.devices

class Lamp(id: String, name: String, var state: Boolean) : Device(id, type, name) {

    companion object {
        val type = "Lamp"
    }
}