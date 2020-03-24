package bedbrains.homesweethomeandroidclient.ui.device

import androidx.lifecycle.ViewModel
import bedbrains.homesweethomeandroidclient.DataRepository

class DevicesViewModel : ViewModel() {

    var devices = DataRepository.devices
}