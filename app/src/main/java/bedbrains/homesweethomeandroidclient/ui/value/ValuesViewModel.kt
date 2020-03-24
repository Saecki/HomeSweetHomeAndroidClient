package bedbrains.homesweethomeandroidclient.ui.value

import androidx.lifecycle.ViewModel
import bedbrains.homesweethomeandroidclient.DataRepository

class ValuesViewModel : ViewModel() {
    val values = DataRepository.values
}