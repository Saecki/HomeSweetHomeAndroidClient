package bedbrains.homesweethomeandroidclient.ui.device.heating

import androidx.lifecycle.ViewModel
import bedbrains.shared.datatypes.devices.Heating
import bedbrains.shared.datatypes.temperature.Temperature

class HeatingViewModel : ViewModel() {
    val heating =
        Heating(
            "lkjfds",
            "My Room",
            "My Heating",
            Temperature(23.0, Temperature.Unit.CELSIUS),
            Temperature(23.0, Temperature.Unit.CELSIUS)
        )
}