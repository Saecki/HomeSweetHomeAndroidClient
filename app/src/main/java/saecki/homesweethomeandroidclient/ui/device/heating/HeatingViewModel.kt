package saecki.homesweethomeandroidclient.ui.device.heating

import androidx.lifecycle.ViewModel
import saecki.homesweethomeandroidclient.datatypes.Temperature
import saecki.homesweethomeandroidclient.datatypes.devices.Heating

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