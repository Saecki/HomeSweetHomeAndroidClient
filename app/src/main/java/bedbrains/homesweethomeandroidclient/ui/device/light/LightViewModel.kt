package bedbrains.homesweethomeandroidclient.ui.device.light

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import bedbrains.homesweethomeandroidclient.DataRepository
import bedbrains.shared.datatypes.devices.Light

class LightViewModel : ViewModel() {
    val light: MutableLiveData<Light?> = MutableLiveData(null)
    var initialCreation = true

    fun observe(lifecycleOwner: LifecycleOwner, uid: String) {
        DataRepository.devices.observe(lifecycleOwner, Observer {
            val matchingDevice = it.find { device -> device.uid == uid }

            light.value = when (matchingDevice) {
                is Light -> matchingDevice
                else -> null
            }
        })
    }
}