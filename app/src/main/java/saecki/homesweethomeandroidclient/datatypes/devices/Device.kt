package saecki.homesweethomeandroidclient.datatypes.devices

abstract class Device(val id: String, val type: Int, var room: String, var name: String) {

    companion object {
        val TYPE = 0
        val ROOM = 1
        val NAME = 2

        fun sortListBy(devices: List<Device>, field: Int): List<Device> {
            when (field) {
                TYPE -> {
                    return devices.sortedBy { device -> device.type }
                }
                ROOM -> {
                    return devices.sortedBy { device -> device.room }
                }
                NAME -> {
                    return devices.sortedBy { device -> device.name }
                }
                else -> {
                    return devices
                }
            }
        }

        fun filterListByTag(devices: List<Device>, tag: String): List<Device> {
            return devices.filter { device -> device.tags.contains(tag) }
        }
    }

    var tags: List<String> = ArrayList()
}