package bedbrains.homesweethomeandroidclient.ui.device.light

import androidx.lifecycle.ViewModel
import bedbrains.shared.datatypes.devices.Light

class LightViewModel : ViewModel() {
    val light = Light("asdflkj", "My Room", "My Light", true)
}