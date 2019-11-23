package saecki.homesweethomeandroidclient.datatypes.devices

class Light(id: String, name: String, room: String, var state: Boolean) :
    Device(id, TYPE, name, room) {

    companion object {
        const val TYPE = 2
    }
}