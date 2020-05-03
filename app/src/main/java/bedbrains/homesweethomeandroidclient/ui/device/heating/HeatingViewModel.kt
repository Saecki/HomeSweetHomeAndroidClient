package bedbrains.homesweethomeandroidclient.ui.device.heating

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import bedbrains.homesweethomeandroidclient.DataRepository
import bedbrains.shared.datatypes.devices.Heating

class HeatingViewModel : ViewModel() {
    val heating: MutableLiveData<Heating?> = MutableLiveData(null)
    var initialCreation = true

    fun observe(lifecycleOwner: LifecycleOwner, uid: String) {
        DataRepository.devices.observe(lifecycleOwner, Observer {
            val matchingDevice = it.find { device -> device.uid == uid }

            heating.value = when (matchingDevice) {
                is Heating -> matchingDevice
                else -> null
            }
        })
    }
}