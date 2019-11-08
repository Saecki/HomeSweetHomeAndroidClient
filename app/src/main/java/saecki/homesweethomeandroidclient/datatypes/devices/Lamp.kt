package saecki.homesweethomeandroidclient.datatypes.devices

class Lamp(id: String, name: String, room: String, var state: Boolean) :
    Device(id, type, name, room) {

    companion object {
        const val type = 2
    }
}