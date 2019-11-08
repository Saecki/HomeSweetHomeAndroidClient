package saecki.homesweethomeandroidclient.ui.home

import androidx.lifecycle.ViewModel
import saecki.homesweethomeandroidclient.datatypes.Temperature
import saecki.homesweethomeandroidclient.datatypes.devices.Device
import saecki.homesweethomeandroidclient.datatypes.devices.Heating
import saecki.homesweethomeandroidclient.datatypes.devices.Lamp

class HomeViewModel : ViewModel() {

    var devices: List<Device> = listOf(
        Heating(
            "idkkek",
            "Heating",
            "Living Room",
            Temperature(24.2341, Temperature.Unit.CELSIUS),
            Temperature(24.0, Temperature.Unit.CELSIUS)
        ),
        Heating(
            "ldssal",
            "Heating",
            "Kitchen",
            Temperature(24.2342, Temperature.Unit.CELSIUS),
            Temperature(24.0, Temperature.Unit.CELSIUS)
        ),
        Lamp("lkjf", "lamp", "Kitchen", true),
        Heating(
            "lkfjasjl",
            "Heating",
            "Office",
            Temperature(23.6324432, Temperature.Unit.CELSIUS),
            Temperature(24.0, Temperature.Unit.CELSIUS)
        ),
        Lamp("oijaisomf", "lamp", "Office", false),
        Heating(
            "lkfjsdfl",
            "Heating",
            "Bathroom",
            Temperature(23.82432, Temperature.Unit.CELSIUS),
            Temperature(24.0, Temperature.Unit.CELSIUS)
        ),
        Lamp("oijaisomf", "lamp", "Bathroom", true)
    )
}