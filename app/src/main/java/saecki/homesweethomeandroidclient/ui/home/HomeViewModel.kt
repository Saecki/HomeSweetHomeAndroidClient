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
            Temperature().apply { setCelsius(24.1) },
            Temperature().apply { setCelsius(24.0) }),
        Heating(
            "ldssal",
            "Kitchen",
            Temperature().apply { setCelsius(24.2) },
            Temperature().apply { setCelsius(24.0) }),
        Lamp("lkjf", "Kitchen", true),
        Heating(
            "lkfjasjl",
            "Office",
            Temperature().apply { setCelsius(23.6) },
            Temperature().apply { setCelsius(24.0) }),
        Lamp("oijaisomf", "Office", false),
        Heating(
            "lkfjasjl",
            "Bathroom",
            Temperature().apply { setCelsius(23.8) },
            Temperature().apply { setCelsius(24.0) }),
        Lamp("oijaisomf", "Bathroom", true)
    )

}