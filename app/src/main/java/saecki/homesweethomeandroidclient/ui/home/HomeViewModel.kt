package saecki.homesweethomeandroidclient.ui.home

import androidx.lifecycle.ViewModel
import saecki.homesweethomeandroidclient.datatypes.devices.Device
import saecki.homesweethomeandroidclient.datatypes.devices.Heating
import saecki.homesweethomeandroidclient.datatypes.devices.Lamp

class HomeViewModel : ViewModel() {

    var devices: List<Device> = listOf(
        Heating("idkkek", "Living Room", 23.4, 24.0),
        Heating("ldssal", "Kitchen", 24.1, 24.0),
        Lamp("lkjf", "Kitchen", true)
    )

}