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
            "Living Room",
            Temperature(24.1, Temperature.Unit.CELSIUS),
            Temperature(24.0, Temperature.Unit.CELSIUS)
        ),
        Heating(
            "ldssal",
            "Kitchen",
            Temperature(24.2, Temperature.Unit.CELSIUS),
            Temperature(24.0, Temperature.Unit.CELSIUS)
        ),
        Lamp("lkjf", "Kitchen", true),
        Heating(
            "lkfjasjl",
            "Office",
            Temperature(23.6, Temperature.Unit.CELSIUS),
            Temperature(24.0, Temperature.Unit.CELSIUS)
        ),
        Lamp("oijaisomf", "Office", false),
        Heating(
            "lkfjasjl",
            "Bathroom",
            Temperature(23.8, Temperature.Unit.CELSIUS),
            Temperature(24.0, Temperature.Unit.CELSIUS)
        ),
        Lamp("oijaisomf", "Bathroom", true)
    )

}