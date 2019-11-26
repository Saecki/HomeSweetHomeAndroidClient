package bedbrains.homesweethomeandroidclient.ui.device.heating

import androidx.lifecycle.ViewModel
import bedbrains.shared.datatypes.Temperature
import bedbrains.shared.datatypes.devices.Heating

class HeatingViewModel : ViewModel() {
    val heating =
        Heating(
            "lkjfds",
            "nice",
            "löskajfdösdl",
            Temperature(23.0, Temperature.Unit.CELSIUS),
            Temperature(23.0, Temperature.Unit.CELSIUS)
        )
}