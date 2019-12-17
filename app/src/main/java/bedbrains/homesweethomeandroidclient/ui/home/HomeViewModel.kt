package bedbrains.homesweethomeandroidclient.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import bedbrains.shared.datatypes.Temperature
import bedbrains.shared.datatypes.devices.Device
import bedbrains.shared.datatypes.devices.Heating
import bedbrains.shared.datatypes.devices.Light

class HomeViewModel : ViewModel() {

    var devices: LiveData<List<Device>> = MutableLiveData(
        listOf(
            Heating(
                "idkkek",
                "Living Room",
                "Heating",
                Temperature(24.2341, Temperature.Unit.CELSIUS),
                Temperature(24.0, Temperature.Unit.CELSIUS)
            ),
            Heating(
                "ldssal",
                "Kitchen",
                "Heating",
                Temperature(24.2342, Temperature.Unit.CELSIUS),
                Temperature(24.0, Temperature.Unit.CELSIUS)
            ),
            Light("lkjf", "Kitchen", "Light", true),
            Heating(
                "lkfjasjl",
                "Office",
                "Heating",
                Temperature(23.6324432, Temperature.Unit.CELSIUS),
                Temperature(24.0, Temperature.Unit.CELSIUS)
            ),
            Light("oijaisomf", "Office", "Light", false),
            Heating(
                "lkfjsdfl",
                "Bathroom",
                "Heating",
                Temperature(23.82432, Temperature.Unit.CELSIUS),
                Temperature(24.0, Temperature.Unit.CELSIUS)
            ),
            Light("oijaisomf", "Bathroom", "Light", true),
            Light("oijaisomf", "Office", "Light", false),
            Heating(
                "lkfjsdfl",
                "Bathroom",
                "Heating",
                Temperature(23.82432, Temperature.Unit.CELSIUS),
                Temperature(24.0, Temperature.Unit.CELSIUS)
            ),
            Light("oijaisomf", "Bathroom", "Light", true),
            Light("oijaisomf", "Office", "Light", false),
            Heating(
                "lkfjsdfl",
                "Bathroom",
                "Heating",
                Temperature(23.82432, Temperature.Unit.CELSIUS),
                Temperature(24.0, Temperature.Unit.CELSIUS)
            ),
            Light("oijaisomf", "Bathroom", "Light", true),
            Light("oijaisomf", "Office", "Light", false),
            Heating(
                "lkfjsdfl",
                "Bathroom",
                "Heating",
                Temperature(23.82432, Temperature.Unit.CELSIUS),
                Temperature(24.0, Temperature.Unit.CELSIUS)
            ),
            Light("oijaisomf", "Bathroom", "Light", true)
        )
    )
}