package saecki.homesweethomeandroidclient.datatypes.devices

class Heating(id: String, name: String, var actualTemp: Double, var targetTemp: Double) :
    Device(id, type, name) {

    companion object {
        val type = "Heating"
    }
}