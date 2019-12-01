package bedbrains.homesweethomeandroidclient.ui.home

import androidx.lifecycle.ViewModel
import bedbrains.shared.datatypes.Temperature
import bedbrains.shared.datatypes.devices.Device
import bedbrains.shared.datatypes.devices.Heating
import bedbrains.shared.datatypes.devices.Light

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
        Light("lkjf", "light", "Kitchen", true),
        Heating(
            "lkfjasjl",
            "Heating",
            "Office",
            Temperature(23.6324432, Temperature.Unit.CELSIUS),
            Temperature(24.0, Temperature.Unit.CELSIUS)
        ),
        Light("oijaisomf", "light", "Office", false),
        Heating(
            "lkfjsdfl",
            "Heating",
            "Bathroom",
            Temperature(23.82432, Temperature.Unit.CELSIUS),
            Temperature(24.0, Temperature.Unit.CELSIUS)
        ),
        Light("oijaisomf", "light", "Bathroom", true),
        Light("oijaisomf", "light", "Office", false),
        Heating(
            "lkfjsdfl",
            "Heating",
            "Bathroom",
            Temperature(23.82432, Temperature.Unit.CELSIUS),
            Temperature(24.0, Temperature.Unit.CELSIUS)
        ),
        Light("oijaisomf", "light", "Bathroom", true),
        Light("oijaisomf", "light", "Office", false),
        Heating(
            "lkfjsdfl",
            "Heating",
            "Bathroom",
            Temperature(23.82432, Temperature.Unit.CELSIUS),
            Temperature(24.0, Temperature.Unit.CELSIUS)
        ),
        Light("oijaisomf", "light", "Bathroom", true),
        Light("oijaisomf", "light", "Office", false),
        Heating(
            "lkfjsdfl",
            "Heating",
            "Bathroom",
            Temperature(23.82432, Temperature.Unit.CELSIUS),
            Temperature(24.0, Temperature.Unit.CELSIUS)
        ),
        Light("oijaisomf", "light", "Bathroom", true)
    )
}